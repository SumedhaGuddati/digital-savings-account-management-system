import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import LoginView from "../views/LoginView.vue";
import MainRegistration from "../views/MainRegistration.vue";
import LandingPageView from "@/views/LandingPageView.vue";
import { setupAuthGuard } from "./authGuard";

const routes: RouteRecordRaw[] = [
  // Public
  { path: "/", component: LandingPageView },
  { path: "/login", component: LoginView },
  { path: "/apply", component: MainRegistration },

  // USER
  {
    path: "/me",
    component: () => import("@/views/user/UserHomeView.vue"),
    meta: { requiresAuth: true }
  },
  {
    path: "/me/application",
    component: () => import("@/views/user/ApplicationStatusView.vue"),
    meta: { requiresAuth: true }
  },
  {
    path: "/me/profile",
    component: () => import("@/views/user/UserProfileView.vue"),
    meta: { requiresAuth: true }
  },
  {
    path: "/me/transactions",
    component: () => import("@/views/user/TransactionsView.vue"),
    meta: { requiresAuth: true }
  },

  // ADMIN
  {
    path: "/admin",
    component: () => import("@/layouts/AdminLayout.vue"),
    meta: { requiresAuth: true, role: "SUPER_ADMIN" },
    children: [
      {
        path: "",
        component: () => import("@/views/admin/DashboardView.vue"),
      },
      {
        path: "approvals",
        component: () => import("@/views/admin/ApprovalsView.vue"),
      },
      {
        path: "audit-logs",
        component: () => import("@/views/admin/AuditLogs.vue"),
      },
    ],
  },

  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFoundView.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

setupAuthGuard(router);

export default router;
