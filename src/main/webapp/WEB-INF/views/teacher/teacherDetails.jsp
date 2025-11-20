<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Teacher Profile"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <div>
        <p class="text-sm uppercase tracking-wide text-slate-500">Teacher Profile</p>
        <h1 class="text-2xl font-semibold text-slate-900">${teacher.name} ${teacher.surname}</h1>
    </div>
    <div class="flex gap-2">
        <a href="${pageContext.request.contextPath}/teachers/edit?id=${teacher.id}"
           class="rounded border border-slate-300 px-4 py-2 text-slate-600">Edit</a>
        <form method="post" action="${pageContext.request.contextPath}/teachers/delete"
              onsubmit="return confirm('Delete this teacher?');">
            <input type="hidden" name="id" value="${teacher.id}">
            <button type="submit" class="rounded bg-rose-600 px-4 py-2 text-white">Delete</button>
        </form>
    </div>
</div>

<section class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
    <dl class="grid gap-4 md:grid-cols-3 text-sm text-slate-600">
        <div><dt class="font-medium text-slate-500">Omang / Passport</dt><dd>${teacher.omangOrPassportNo}</dd></div>
        <div><dt class="font-medium text-slate-500">Contact</dt><dd>${teacher.contactNo}</dd></div>
        <div><dt class="font-medium text-slate-500">Email</dt><dd>${teacher.email}</dd></div>
        <div><dt class="font-medium text-slate-500">Gender</dt><dd>${teacher.gender}</dd></div>
        <div><dt class="font-medium text-slate-500">Date Joined</dt><dd>${teacher.dateJoined}</dd></div>
        <div><dt class="font-medium text-slate-500">Qualifications</dt><dd>${teacher.qualifications}</dd></div>
    </dl>
    <p class="mt-4 text-sm text-slate-500">${teacher.address}</p>
</section>

<section class="mt-8 grid gap-4 md:grid-cols-2">
    <div class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 class="text-lg font-semibold text-slate-900 mb-3">Subjects Qualified</h2>
        <ul class="list-disc pl-5 space-y-1 text-sm text-slate-600">
            <c:forEach var="subject" items="${teacher.subjectsQualifiedToTeach}">
                <li>${subject}</li>
            </c:forEach>
            <c:if test="${empty teacher.subjectsQualifiedToTeach}">
                <li>No subjects captured yet.</li>
            </c:if>
        </ul>
    </div>
    <div class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 class="text-lg font-semibold text-slate-900 mb-3">Class Assignments</h2>
        <ul class="space-y-2 text-sm text-slate-600">
            <c:forEach var="entry" items="${teacher.classToSubjectMap}">
                <li class="flex justify-between border-b border-slate-100 pb-2">
                    <span class="font-medium">${entry.key}</span>
                    <span>${entry.value}</span>
                </li>
            </c:forEach>
            <c:if test="${empty teacher.classToSubjectMap}">
                <li>No class assignments captured yet.</li>
            </c:if>
        </ul>
    </div>
</section>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

