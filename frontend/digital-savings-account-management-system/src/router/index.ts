import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import LoginView from "../views/LoginView.vue";
import MainRegistraion from "../views/MainRegistration.vue";
import LandingPageView from "@/views/LandingPageView.vue";
import ApprovalsView from "@/views/ApprovalsView.vue";
import AuditLogs from "@/views/AuditLogs.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    name: "home",
    component: LandingPageView,
  },
  { path: "/login", component: LoginView },
  { path: "/apply", component: MainRegistraion },
  { path: '/me', component: () => import("../views/user/UserHomeView.vue") },
  { path: '/me/application', component: () => import("../views/user/ApplicationStatusView.vue") },
  { path: '/me/profile', component: () => import("../views/user/UserProfileView.vue") },
  { path: '/me/transactions', component: () => import("../views/user/TransactionsView.vue") },
  { path: "/approvals", component: ApprovalsView },
  { path: "/audit-logs", component: AuditLogs },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;