<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Parent Registration"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="mx-auto max-w-2xl rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
    <h1 class="text-2xl font-semibold text-center text-slate-900 mb-2">Parent Access Registration</h1>
    <p class="text-center text-sm text-slate-500 mb-6">
        Register to follow your child's progress. Accounts are verified by the administrator.
    </p>

    <c:if test="${not empty error}">
        <div class="mb-4 rounded border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">
            ${error}
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/register" class="grid gap-5 md:grid-cols-2">
        <label class="text-sm font-medium text-slate-600">First Name
            <input type="text" name="name" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Surname
            <input type="text" name="surname" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Contact Number
            <input type="text" name="contactNo" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Email Address
            <input type="email" name="email" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Username
            <input type="text" name="username" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Password
            <input type="password" name="password" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Confirm Password
            <input type="password" name="confirmPassword" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <div class="md:col-span-2 flex justify-end gap-3 pt-2">
            <a href="${pageContext.request.contextPath}/auth/login"
               class="rounded border border-slate-300 px-4 py-2 text-slate-600">Cancel</a>
            <button type="submit" class="rounded bg-sky-700 px-5 py-2 text-white">Register</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

