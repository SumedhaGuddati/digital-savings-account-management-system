import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import LoginView from "../views/LoginView.vue";
import MainRegistraion from "../views/MainRegistration.vue";
import TransactionsView from '@/views/TransactionsView.vue'
import LandingPageView from "@/views/LandingPageView.vue";
import UserHomeView from "@/views/UserHomeView.vue";
import UserProfileView from "@/views/UserProfileView.vue";
import DashboardView from "../views/DashboardView.vue";
import ApprovalsView from "../views/ApprovalsView.vue";
import AuditLogs from "../views/AuditLogs.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    name: "home",
    component: LandingPageView,
  },
  { path: "/login", component: LoginView },
  { path: "/apply", component: MainRegistraion },
  { path: '/me', component: UserHomeView },
  { path: '/me/application', component: () => import("../views/ApplicationStatusView.vue") },
  { path: '/me/profile', component: UserProfileView },
  { path: '/me/transactions', component: TransactionsView },
  { path: "/approvals", component: ApprovalsView },
  { path: "/audit-logs", component: AuditLogs },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;