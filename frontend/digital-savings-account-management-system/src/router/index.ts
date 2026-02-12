import { createRouter, createWebHistory } from "vue-router";

import DashboardView from "../views/DashboardView.vue";
import ApprovalsView from "../views/ApprovalsView.vue";
import AuditLogs from "../views/AuditLogs.vue";

const routes = [
  { path: "/", component: DashboardView },
  { path: "/approvals", component: ApprovalsView },
  { path: "/audit-logs", component: AuditLogs },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
