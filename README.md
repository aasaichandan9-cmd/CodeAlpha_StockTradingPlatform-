# Stock Trading Platform 📈

A Java console application built as part of the **CodeAlpha Java Programming Internship**.

## Features
- Simulated stock market with **12 real-world stocks** (AAPL, GOOGL, MSFT, TSLA, NVDA, and more)
- **Buy and sell** stocks with cash balance validation
- **Portfolio dashboard** showing holdings and total asset value
- **Transaction history** with timestamps
- **Market price simulation** — prices fluctuate ±5% each turn
- Saves portfolio and market data via **CSV file persistence**

## Tech Stack
- Language: **Java**
- Concepts: OOP, HashMap, ArrayList, File I/O, LocalDateTime

## Project Structure
```
src/
├── Stock.java          # Stock entity with price tracking
├── Market.java         # 12 stocks + price simulation engine
├── Portfolio.java      # Holdings (symbol → quantity map)
├── Transaction.java    # Buy/sell records with timestamps
├── User.java           # User account (balance, portfolio, history)
├── FileManager.java    # CSV persistence
└── Main.java           # Console menu UI
```

## How to Run
```bash
# Compile
javac -d out src/*.java

# Run
java -cp out Main
```

## Author
Developed for **CodeAlpha Java Programming Internship**
