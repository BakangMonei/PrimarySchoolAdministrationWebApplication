<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Login"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="mx-auto max-w-md rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
    <h1 class="text-2xl font-semibold text-center text-slate-900 mb-2">Welcome Back</h1>
    <p class="text-center text-sm text-slate-500 mb-6">Sign in to access the administration portal.</p>

    <c:if test="${not empty error}">
        <div class="mb-4 rounded border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">
            ${error}
        </div>
    </c:if>
    <c:if test="${param.registered eq 'true'}">
        <div class="mb-4 rounded border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">
            Registration successful. Please sign in.
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/login" class="space-y-4">
        <label class="text-sm font-medium text-slate-600">Username
            <input type="text" name="username" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Password
            <input type="password" name="password" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="flex items-center gap-2 text-sm text-slate-600">
            <input type="checkbox" name="rememberMe" class="rounded border-slate-300 text-sky-700">
            Remember me (7 days)
        </label>
        <button type="submit" class="w-full rounded bg-sky-700 px-4 py-2 text-white">Sign In</button>
    </form>
    <p class="mt-6 text-center text-sm text-slate-500">
        Need access? <a href="${pageContext.request.contextPath}/auth/register" class="text-sky-700 hover:underline">Register as Parent</a>
    </p>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

