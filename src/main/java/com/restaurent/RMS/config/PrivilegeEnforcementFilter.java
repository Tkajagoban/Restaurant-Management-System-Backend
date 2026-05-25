package com.restaurent.RMS.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PrivilegeEnforcementFilter extends OncePerRequestFilter {

    private static final Map<String, String> URI_TO_PRIVILEGE = new HashMap<>();

    static {
        // Map common URI path segments to Privilege Names as defined in DataLoader
        URI_TO_PRIVILEGE.put("/roles", "Role Management");
        URI_TO_PRIVILEGE.put("/users", "User Management");
        URI_TO_PRIVILEGE.put("/rolePrivilege", "Role Privileges");
        URI_TO_PRIVILEGE.put("/restaurantPrivilege", "Restaurant Privilege");
        URI_TO_PRIVILEGE.put("/email", "Email Settings");
        URI_TO_PRIVILEGE.put("/tax", "Tax Settings");
        URI_TO_PRIVILEGE.put("/restaurant", "Restaurant Management");
        URI_TO_PRIVILEGE.put("/food", "Food Management");
        URI_TO_PRIVILEGE.put("/MainCategories", "Food Management");
        URI_TO_PRIVILEGE.put("/SubCategory", "Food Management");
        URI_TO_PRIVILEGE.put("/table", "Table Management");
        URI_TO_PRIVILEGE.put("/orderManagement", "Order Management");
        URI_TO_PRIVILEGE.put("/ordersummary", "Order Management"); // Orders are part of Order Management
        URI_TO_PRIVILEGE.put("/orders", "Order Management");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        // Allow all GET requests
        if ("GET".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check for state-changing requests (POST, PUT, DELETE, PATCH)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            String requiredPrivilege = null;
            for (Map.Entry<String, String> entry : URI_TO_PRIVILEGE.entrySet()) {
                if (uri.contains(entry.getKey())) {
                    requiredPrivilege = entry.getValue();
                    break;
                }
            }

            if (requiredPrivilege != null) {
                final String readAuthority = requiredPrivilege + ":READ";
                boolean hasReadStatus = authentication.getAuthorities().stream()
                        .anyMatch(a -> readAuthority.equals(a.getAuthority()));

                if (hasReadStatus) {
                    // Block state-changing requests for READ-only status
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write(
                            "{\"errorCode\": 403, \"statusMessage\": \"ACCESS DENIED: Your privilege status is READ and cannot perform "
                                    + method + " operations for " + requiredPrivilege + "\"}");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
