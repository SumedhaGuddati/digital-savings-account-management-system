# Digital Savings Account Management System

A full-stack banking application that allows customers to apply for and manage digital savings accounts, and enables admins to review, approve, and monitor accounts securely.

Built using:
- **Backend:** Spring Boot, PostgreSQL, JWT Security
- **Frontend:** Vue.js, TypeScript, TailwindCSS

---

## 📁 Project Structure
```
digital-savings-account-management-system/
├── backend/ # Spring Boot application (DigiSavingsCore)
├── frontend/ # Vue.js application
├── docker/ # Dockerfiles and docker-compose configs
└── README.md
```


---

## 🚀 How to Run the Application (Local)

### 1. Clone the repository
```bash
git clone <repo-url>
cd zeta-digital-savings-account-management-system
```

### 2. Run backend
```bash
cd backend/digisavings-core
mvn spring-boot:run
```
Backend runs at: http://localhost:8080


### 3. Run frontend
```bash
cd frontend/digital-savings-account-management-system
npm install
npm run serve
```
Frontend runs at: http://localhost:5173


## Module Documentation
Backend details → [/backend/README.md](./backend/digisavings-core/README.md)

Frontend details → [/frontend/README.md](./frontend/digital-savings-account-management-system/README.md)


## Team & Contribution

This project is developed as a capstone project using a collaborative full-stack workflow with Git, Spring Boot and Vue.js.