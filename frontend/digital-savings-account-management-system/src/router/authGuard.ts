import { Router } from "vue-router";
import store from "@/store";
import { TOKEN_KEY } from "@/utils/auth.token";

export function setupAuthGuard(router: Router) {
    router.beforeEach(async (to, from, next) => {
        const token = sessionStorage.getItem(TOKEN_KEY);

        if (to.meta.requiresAuth && !token) {
            return next("/");
        }

        const role = store.state.auth.user?.role;

        // ADMIN ROUTES
        if (to.path.startsWith("/admin")) {
            if (role !== "ADMIN" && role !== "SUPER_ADMIN") {
                store.dispatch("auth/logout");
                return next({ name: "NotFound" });
            }
            return next();
        }

        // USER ROUTES
        if (to.path.startsWith("/me")) {
            if (role !== "USER") {
                store.dispatch("auth/logout");
                return next({ name: "NotFound" });
            }

            // Fetch account status from Vuex store
            let status = store.getters["auth/accountStatus"];

            if (status === "NONE" || !status) {
                try {
                    status = await store.dispatch("auth/fetchAccountStatus");
                } catch (error) {
                    console.error("Failed to fetch account status:", error);
                    return next("/me/application");
                }
            }

            if (["PENDING", "REJECTED"].includes(status)) {
                if (to.path === "/me/application") {
                    return next();
                }
                return next("/me/application");
            }

            if (status === "APPROVED") {
                return next();
            }

            if (status === "NONE") {
                return next("/");
            }

            return next("/me/application");
        }
        next();
    });
}
