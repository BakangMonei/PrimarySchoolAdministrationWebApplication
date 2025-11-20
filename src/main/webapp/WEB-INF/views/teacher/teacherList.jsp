<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Teachers"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <div>
        <h1 class="text-2xl font-semibold text-slate-900">Teachers</h1>
        <p class="text-sm text-slate-500">Manage teacher profiles, qualifications, and class assignments.</p>
    </div>
    <a href="${pageContext.request.contextPath}/teachers/new"
       class="rounded-lg bg-sky-700 px-4 py-2 text-white shadow hover:bg-sky-600">Add Teacher</a>
</div>

<form method="get" class="mb-6 flex flex-wrap gap-3 rounded-xl border border-slate-200 bg-white p-4">
    <input type="text" name="name" value="${filters.name}" placeholder="Name or surname"
           class="flex-1 rounded border border-slate-300 px-3 py-2 text-sm">
    <input type="text" name="subject" value="${filters.subject}" placeholder="Subject"
           class="flex-1 rounded border border-slate-300 px-3 py-2 text-sm">
    <button type="submit" class="rounded bg-emerald-600 px-4 py-2 text-white text-sm">Search</button>
</form>

<div class="space-y-4">
    <c:forEach var="teacher" items="${teachers}">
        <article class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <div class="flex flex-wrap items-center justify-between gap-3">
                <div>
                    <h2 class="text-xl font-semibold text-slate-900">
                        ${teacher.name} ${teacher.surname}
                    </h2>
                    <p class="text-sm text-slate-500">Subjects: ${teacher.subjectsQualifiedCsv}</p>
                </div>
                <div class="flex gap-2">
                    <a href="${pageContext.request.contextPath}/teachers/view?id=${teacher.id}"
                       class="rounded border border-slate-300 px-4 py-2 text-slate-600">View</a>
                    <a href="${pageContext.request.contextPath}/teachers/edit?id=${teacher.id}"
                       class="rounded bg-sky-700 px-4 py-2 text-white">Edit</a>
                </div>
            </div>
            <dl class="mt-4 grid gap-4 text-sm text-slate-600 md:grid-cols-4">
                <div><dt class="font-semibold text-slate-500">Omang/Passport</dt><dd>${teacher.omangOrPassportNo}</dd></div>
                <div><dt class="font-semibold text-slate-500">Email</dt><dd>${teacher.email}</dd></div>
                <div><dt class="font-semibold text-slate-500">Contact</dt><dd>${teacher.contactNo}</dd></div>
                <div><dt class="font-semibold text-slate-500">Joined</dt><dd>${teacher.dateJoined}</dd></div>
            </dl>
        </article>
    </c:forEach>
    <c:if test="${empty teachers}">
        <p class="rounded border border-slate-200 bg-white px-4 py-6 text-center text-sm text-slate-500">No teachers found.</p>
    </c:if>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

