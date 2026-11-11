package com.home.expenses_control.service;

import com.home.expenses_control.dto.response.PaginationResponseDto;
import com.home.expenses_control.dto.request.TransactionRequestDto;
import com.home.expenses_control.dto.response.TransactionResponseDto;
import com.home.expenses_control.dto.response.TransactionResponseDtoPaginated;
import com.home.expenses_control.enums.Status;
import com.home.expenses_control.exceptions.*;
import com.home.expenses_control.mapper.TransactionMapper;
import com.home.expenses_control.model.Category;
import com.home.expenses_control.model.Transaction;
import com.home.expenses_control.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final int MAX_PAGE_SIZE = 50;

    private final CategoryService categoryService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(CategoryService categoryService, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.categoryService = categoryService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public TransactionResponseDto registerNewTransaction(TransactionRequestDto transactionData) {
        // check if amount is greater than or equal to 0
        validateTransactionAmount(transactionData.amount());

        // get the category model by id
        Category transactionCategory = getActiveCategoryById(transactionData.categoryId());

        // create a new transaction model
        Transaction newTransaction = transactionMapper.fromDtoToModel(transactionData, transactionCategory);
        return transactionMapper.fromModelToDto(transactionRepository.saveTransaction(newTransaction));
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionById(UUID transactionId) {
        return transactionMapper.fromModelToDto(getTransactionModelById(transactionId));
    }

    @Override
    @Transactional
    public TransactionResponseDto updateTransaction(UUID transactionId, TransactionRequestDto newTransactionData) {
        Transaction transaction = getTransactionModelById(transactionId);
        Category category = transaction.getCategory();
        UUID currentCategoryId = category.getId();

        // check if category changed;
        // if changed, get new category
        category = currentCategoryId.equals(newTransactionData.categoryId()) ? category : getActiveCategoryById(newTransactionData.categoryId());

        // check if amount changed;
        // if changed, validate new amount
        if(transaction.getAmount().compareTo(newTransactionData.amount()) != 0) {
            validateTransactionAmount(newTransactionData.amount());
        }

        transaction.update(newTransactionData.type(), newTransactionData.amount(), newTransactionData.date(), category, newTransactionData.note());

        return transactionMapper.fromModelToDto(transactionRepository.saveTransaction(transaction));

    }

    @Override
    @Transactional
    public TransactionResponseDto deactivateTransaction(UUID transactionId) {
        // get transaction
        Transaction transaction = getTransactionModelById(transactionId);

        // check current status
        if(transaction.getStatus() == Status.INACTIVE){
            throw new TransactionAlreadyInactiveException();
        }

        transaction.deactivate();
        return transactionMapper.fromModelToDto(transactionRepository.saveTransaction(transaction));
    }

    @Override
    @Transactional
    public TransactionResponseDto activateTransaction(UUID transactionId) {
        // get transaction
        Transaction transaction = getTransactionModelById(transactionId);

        // check current status
        if(transaction.getStatus() == Status.ACTIVE){
            throw new TransactionAlreadyActiveException();
        }

        transaction.activate();
        return transactionMapper.fromModelToDto(transactionRepository.saveTransaction(transaction));
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDtoPaginated getTransactionsList(Integer page, Integer pageSize, Status status, LocalDate from, LocalDate to) {
        validatePagination(page, pageSize);
        validateDates(from, to);

        // make sure ACTIVE is always the default case
        Status searchStatus = status != null ? status : Status.ACTIVE;;

        Specification<Transaction> spec = (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("status"), searchStatus);

        if(from != null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("date"), from, to));
        }

        Page<Transaction> transactionPage = transactionRepository.getAllTransactions(spec, page, pageSize);

        var transactions = transactionPage.getContent().stream().map(transactionMapper::fromModelToDto).toList();
        var pagination = new PaginationResponseDto(
            transactionPage.getNumber() + 1,
            transactionPage.getSize(),
            transactionPage.getTotalElements(),
            transactionPage.getTotalPages()
        );

        return new TransactionResponseDtoPaginated(transactions, pagination);
    }

    private Transaction getTransactionModelById(UUID transactionId) {
        return transactionRepository.getTransactionById(transactionId);
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new NegativeValueException();
        }
    }

    private Category getActiveCategoryById(UUID categoryId){

        Category category = categoryService.getCategoryModelById(categoryId);

        // validates if category is active
        if (category.getStatus().equals(Status.INACTIVE)){
            throw new InactiveCategoryException("Category used is currently inactive");
        }

        return category;
    }

    private void validatePagination(Integer page, Integer pageSize){
        if(page == null || page < 1){
            throw new InvalidPageException("Page number must exist and be greater than zero");
        }

        if(pageSize == null || pageSize < 1){
            throw new InvalidPageSizeException("Page size must exist and be greater than zero");
        }

        if(pageSize > MAX_PAGE_SIZE){
            throw new InvalidPageSizeException("Page size must not exceed " + MAX_PAGE_SIZE);
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate){
        if(startDate == null && endDate == null) return;

        if(startDate == null || endDate == null) {
            throw new InvalidDateException("Both start and end date must be informed together.");
        }

        if(startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date.");
        }
    }
}
