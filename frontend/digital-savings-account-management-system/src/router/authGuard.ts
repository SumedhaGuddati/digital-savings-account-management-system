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

        if (to.path.startsWith("/admin")) {
            if (role !== "ADMIN" && role !== "SUPER_ADMIN") {
                store.dispatch("auth/logout");
                return next({ name: "NotFound" });
            }
            return next();
        }

        if (to.path.startsWith("/me")) {
            if (role !== "USER") {
                store.dispatch("auth/logout");
                return next({ name: "NotFound" });
            }
            else if (to.meta.requiresAuth && role === "USER") {

                if (!sessionStorage.getItem("accountStatus")) {
                    await store.dispatch("auth/fetchAccountStatus");
                }

                const status = store.getters["auth/accountStatus"];

                if (status === "APPROVED" && to.path === "/") {
                    return next("/me");
                }

                if (to.path === "/me/application") {
                    return next();
                }

                if (
                    (to.path === "/me" ||
                        to.path.startsWith("/me/profile") ||
                        to.path.startsWith("/me/transactions")) &&
                    status !== "APPROVED"
                ) {
                    return next("/me/application");
                }

                if (to.path === "/apply") {
                    if (status === "PENDING") {
                        return next("/me/application");
                    }
                }

                return next();
            }
        }

        next();
    });
}
