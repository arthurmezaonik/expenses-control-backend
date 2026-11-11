# Personal Finance Tracker

An educational Java portfolio project: a single-user REST API for recording personal income and expenses and reviewing cash flow over a date range.

## Goal

Help someone understand their cash flow by recording transactions and viewing accurate, explainable summaries.

## Initial scope

- Java backend exposed as a REST API.
- Persistent storage so data survives app restarts.
- One user, with no login or account system.
- All amounts stored and displayed in GBP.
- No bank connections, currency conversion, or AI features in the first version.
- A frontend is deferred; API clients such as `curl` or Postman can be used during backend development.

## User stories

- As a user, I can record income or an expense with an amount, date, category, and optional note.
- As a user, I can list transactions, newest first.
- As a user, I can edit a transaction and deactivate or reactivate it.
- As a user, I can manage the categories available for transactions.
- As a user, I can request a summary for a date range and see income, expenses, net cash flow, and the transactions in that range.

## Domain rules

- Amounts must be greater than zero. The transaction type (`INCOME` or `EXPENSE`) determines how the amount contributes to totals.
- Amounts use GBP. Currency conversion and per-account currency settings are out of scope.
- A transaction requires a date and category; its note is optional.
- Date-range boundaries are inclusive (`from <= transaction date <= to`).
- Summaries exclude inactive transactions by default.
- Transactions are deactivated rather than deleted. Inactive transactions remain stored and can be reactivated; they are excluded from summaries by default.
- Transaction lists include active transactions by default. `includeInactive=true` includes inactive transactions.
- Categories can be created, renamed, and deactivated. Deactivating a category preserves historical transactions and prevents the category from being selected for new transactions.

## Starter categories

- Rent or mortgage
- Bills
- Groceries
- Restaurants
- Leisure
- Hobbies
- Other

## API outline

All routes are under `/api`.

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/transactions` | Create a transaction |
| `GET` | `/transactions` | List transactions, newest first; supports optional `from`, `to`, and `includeInactive` filters |
| `GET` | `/transactions/{id}` | Get one transaction |
| `PUT` | `/transactions/{id}` | Edit a transaction |
| `PATCH` | `/transactions/{id}/status` | Deactivate or reactivate a transaction |
| `GET` | `/categories` | List categories |
| `POST` | `/categories` | Create a category |
| `PUT` | `/categories/{id}` | Rename or edit a category |
| `PATCH` | `/categories/{id}/status` | Deactivate or reactivate a category |
| `GET` | `/summary?from=YYYY-MM-DD&to=YYYY-MM-DD` | Return totals and matching transactions for an inclusive date range |

### Summary response sketch

The summary includes matching active transactions as well as totals. A future frontend can use the transaction list to create daily or category breakdowns without another request.

```json
{
  "from": "2026-10-01",
  "to": "2026-10-31",
  "currency": "GBP",
  "totalIncome": 3200.00,
  "totalExpenses": 845.50,
  "net": 2354.50,
  "transactions": [
    {
      "id": 12,
      "type": "EXPENSE",
      "amount": 45.00,
      "date": "2026-10-08",
      "category": "Groceries",
      "note": "Weekly shop"
    }
  ]
}
```

## Acceptance criteria for the first version

- Valid transactions can be created, listed, and edited; changes persist after restarting the app.
- Missing required fields and amounts less than or equal to zero are rejected.
- Transaction lists are ordered newest first and omit inactive entries unless requested.
- Deactivated transactions remain retrievable and can be reactivated.
- A summary returns the requested inclusive date range, GBP income/expense/net totals, and its active transactions.
- Summary totals agree with the included transactions.
- Categories can be managed; inactive categories remain associated with historical transactions but cannot be used for new transactions.

## Build sequence

1. Define transaction and category models and their validation rules.
2. Implement transaction and category persistence.
3. Add transaction and category REST operations.
4. Implement inclusive date-range summaries and ensure totals match returned transactions.
5. Document how to run the API and exercise it with example requests.
6. Consider a frontend as a later phase.

## Decisions to revisit

- Choose the database and Java framework during project setup.
- Decide whether inactive transactions need an explicit history or undo mechanism.
- Decide whether list endpoints need pagination once there is enough data to make it useful.

## Run the app locally with Docker Compose

The parent workspace contains the Docker Compose setup for PostgreSQL, the Spring Boot API, and the React development server. See the workspace `README.md` for startup instructions.

1. Find your computer's Wi-Fi IP address with `ipconfig getifaddr en0`.
2. Copy `.env.example` to `.env` and set `LAN_HOST` to that IP. Replace the example database password and initial account details with your own values.
3. From this folder, run `docker compose up --build`.
4. Open `http://<LAN_HOST>:5173` on your computer or a phone on the same Wi-Fi. The API is at `http://<LAN_HOST>:8080`.

The database is only reachable by the backend on Compose's private network, so it does not conflict with or expose any PostgreSQL server on the computer. `docker compose down` stops the services while keeping the database volume. The React development server watches the frontend checkout, so frontend edits appear without rebuilding the image. Rebuild the backend after backend code changes with `docker compose up --build backend`.
