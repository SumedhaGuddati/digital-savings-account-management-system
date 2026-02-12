import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import MainRegistration from "../views/MainRegistration.vue";
import DashboardView from "../views/DashboardView.vue";
import ApprovalsView from "../views/ApprovalsView.vue";
import AuditLogs from "../views/AuditLogs.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: DashboardView,
  },
  {
    path: "/about",
    name: "about",
    component: () => import("../views/AboutView.vue"),
  },
  { path: "/approvals", component: ApprovalsView },
  { path: "/audit-logs", component: AuditLogs },
  {
    path: "/apply",
    name: "Apply",
    component: MainRegistration,
  },
  {
    path: "/status",
    name: "ApplicationStatus",
    component: () => import("../views/ApplicationStatus.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;