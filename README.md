# GetPayed POS

Android Point-of-Sale application developed with Kotlin and Jetpack Compose.  
It supports offline transaction storage, background synchronization, and payment simulation features.

---

## Overview

GetPayed POS allows users to record various transactions (Cash, Card, and Transfer), simulate incoming transfers for immediate balance updates, and view comprehensive reports, including End-of-Day summaries.  
The application is built to demonstrate clean architecture, offline-first data handling, and modern Android development practices using the latest Jetpack libraries.

---

## Key Features

- **Offline Transaction Management:** Record and manage offline transactions using Room as the local source of truth.  
- **Multiple Payment Methods:** Supports transactions via Cash, Card, and Transfer.  
- **Transfer Simulation:** Simulate incoming transfers with random sender details and a distinct sound notification.  
- **Data Synchronization:** Automatic and manual synchronization of local data using WorkManager.  
- **Dashboard:** Live view of the current balance and account details.  
- **History & Reporting:** Detailed transaction history with filtering, sync status, and End-of-Day report.  
- **Reactive UI:** Built with Koin for dependency injection and Kotlin Flows for reactive data updates.

---

## Tech Stack

| Category              | Component               | Dependency / Notes                     |
|-----------------------|------------------------|----------------------------------------|
| **Language**          | Kotlin                 | Modern, concise language               |
| **UI**                | Jetpack Compose        | Declarative UI (Material 3)            |
| **Architecture**      | ViewModel & Flow       | Reactive, lifecycle-aware data handling|
| **Data Persistence**  | Room (KSP)             | Local database management              |
| **Asynchronicity**    | Coroutines & Flow      | Structured concurrency                 |
| **Background Sync**   | WorkManager            | Reliable background scheduling         |
| **Dependency Injection** | Koin               | Lightweight DI framework               |
| **Networking**        | Retrofit & OkHttp      | REST client with Mock Interceptor      |
| **Location**          | Play Services Location | Geolocation services                   |

---

## Setup Instructions

To get a local copy up and running, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/gwabstech/GetPayedPOS.git
   cd GetPayedPOS
