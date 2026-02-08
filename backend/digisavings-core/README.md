# DigiSavingsCore

Backend system for managing digital savings accounts.

```declarative
com.company.digisavingscore
├── DigiSavingsCoreApplication.java
├── config               // App-level configs (security, swagger, etc.)
├── controller           // REST controllers
├── dto                  // Request/response DTOs
├── entity               // JPA Entities
├── exception            // Custom exceptions & handlers
├── mapper               // Mapping classes (Entity ↔ DTO)
├── repository           // Spring Data JPA repositories
├── service
│   └── impl             // Service implementations
├── security             // JWT, auth filters, UserDetails, etc.
├── util                 // Utility classes (helpers)

```

## Usage

### Running PostgreSQL on Kubernetes

1. Apply the PostgreSQL deployment:

```bash
kubectl apply -f src/main/resources/postgres.yaml
```

2. Check the running pods:

```bash
kubectl get pods
```

3. Check the services:

```bash
kubectl get svc
```

4. Forward the PostgreSQL port to your local machine:

```bash
kubectl port-forward pod/<postgres-pod-name> 5432:5432
```

5. Open Rancher & check for:
- Open Rancher Dashboard
- Go to your Cluster → Workloads
- Look for: postgres

