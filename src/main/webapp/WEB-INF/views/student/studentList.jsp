<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="statuses" value="${fn:split('Active,Inactive,Transferred,Graduated', ',')}"/>
<c:set var="pageTitle" value="Students"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <div>
        <h1 class="text-2xl font-semibold text-slate-900">Students</h1>
        <p class="text-sm text-slate-500">Filter by status, class, or guardian details.</p>
    </div>
    <a href="${pageContext.request.contextPath}/students/new"
       class="rounded-lg bg-sky-700 px-4 py-2 text-white shadow hover:bg-sky-600">Add Student</a>
 </div>

<c:if test="${not empty param.success}">
    <div class="mb-4 rounded border border-emerald-200 bg-emerald-50 px-4 py-3 text-emerald-800">
        Student <c:out value="${param.success}"/> successfully.
    </div>
</c:if>

<form method="get" class="grid gap-4 rounded-xl border border-slate-200 bg-white p-4 md:grid-cols-5">
    <input type="text" name="name" value="${filters.name}" placeholder="Name"
           class="rounded border border-slate-300 px-3 py-2 text-sm">
    <input type="text" name="surname" value="${filters.surname}" placeholder="Surname"
           class="rounded border border-slate-300 px-3 py-2 text-sm">
    <input type="text" name="class" value="${filters.class}" placeholder="Class (e.g., Standard 4)"
           class="rounded border border-slate-300 px-3 py-2 text-sm">
    <select name="status" class="rounded border border-slate-300 px-3 py-2 text-sm">
        <option value="">Any Status</option>
        <c:forEach var="status" items="${statuses}">
            <option value="${status}" <c:if test="${filters.status == status}">selected</c:if>>${status}</option>
        </c:forEach>
    </select>
    <input type="date" name="dob" value="${filters.dob}" class="rounded border border-slate-300 px-3 py-2 text-sm">
    <button type="submit"
            class="col-span-1 rounded bg-emerald-600 px-4 py-2 text-white hover:bg-emerald-500">Search</button>
</form>

<div class="mt-6 overflow-x-auto rounded-xl border border-slate-200 bg-white shadow-sm">
    <table class="min-w-full divide-y divide-slate-200 text-sm">
        <thead class="bg-slate-100">
        <tr>
            <th class="px-4 py-3 text-left font-semibold text-slate-600">Name</th>
            <th class="px-4 py-3 text-left font-semibold text-slate-600">Birth Certificate #</th>
            <th class="px-4 py-3 text-left font-semibold text-slate-600">Class</th>
            <th class="px-4 py-3 text-left font-semibold text-slate-600">Status</th>
            <th class="px-4 py-3 text-left font-semibold text-slate-600">Average Grade</th>
            <th class="px-4 py-3 text-left font-semibold text-slate-600"></th>
        </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
        <c:choose>
            <c:when test="${empty students}">
                <tr>
                    <td colspan="6" class="px-4 py-6 text-center text-slate-500">
                        No students match the search criteria.
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="student" items="${students}">
                    <tr class="hover:bg-slate-50">
                        <td class="px-4 py-3 font-medium text-slate-800">
                            <c:out value="${student.name}"/> <c:out value="${student.surname}"/>
                        </td>
                        <td class="px-4 py-3 text-slate-600">
                            <c:out value="${student.birthCertificateNo}"/>
                        </td>
                        <td class="px-4 py-3"><c:out value="${student.currentClass}"/></td>
                        <td class="px-4 py-3">
                            <span class="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-600">
                                <c:out value="${student.status}"/>
                            </span>
                        </td>
                        <td class="px-4 py-3">
                            <c:out value="${student.averageGrade}"/>%
                        </td>
                        <td class="px-4 py-3 text-right">
                            <a href="${pageContext.request.contextPath}/students/view?id=${student.id}"
                               class="text-sky-700 hover:underline">View</a>
                            <span class="px-1 text-slate-400">|</span>
                            <a href="${pageContext.request.contextPath}/students/edit?id=${student.id}"
                               class="text-amber-600 hover:underline">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

