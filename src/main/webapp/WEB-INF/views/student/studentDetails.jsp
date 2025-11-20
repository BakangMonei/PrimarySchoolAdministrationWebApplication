<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Student Profile"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<c:set var="student" value="${selectedStudent}"/>

<div class="flex items-center justify-between mb-6">
    <div>
        <p class="text-sm uppercase tracking-wide text-slate-500">Student Profile</p>
        <h1 class="text-2xl font-semibold text-slate-900">
            <c:out value="${student.name}"/> <c:out value="${student.surname}"/>
        </h1>
    </div>
    <div class="flex gap-3">
        <a href="${pageContext.request.contextPath}/students/edit?id=${student.id}"
           class="rounded border border-slate-300 px-4 py-2 text-slate-600">Edit</a>
        <form method="post" action="${pageContext.request.contextPath}/students/delete"
              onsubmit="return confirm('Are you sure you want to delete this student?');">
            <input type="hidden" name="id" value="${student.id}">
            <button type="submit" class="rounded bg-rose-600 px-4 py-2 text-white">Delete</button>
        </form>
    </div>
</div>

<div class="grid gap-6 md:grid-cols-2">
    <section class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 class="text-lg font-semibold text-slate-900 mb-4">Personal Details</h2>
        <dl class="space-y-3 text-sm text-slate-600">
            <div class="flex justify-between"><dt>Birth Certificate #</dt><dd>${student.birthCertificateNo}</dd></div>
            <div class="flex justify-between"><dt>Gender</dt><dd>${student.gender}</dd></div>
            <div class="flex justify-between"><dt>Date of Birth</dt><dd><fmt:formatDate value="${student.dateOfBirth}" pattern="dd MMM yyyy"/></dd></div>
            <div class="flex justify-between"><dt>Current Class</dt><dd>${student.currentClass}</dd></div>
            <div class="flex justify-between"><dt>Status</dt><dd>${student.status}</dd></div>
            <div class="flex justify-between"><dt>Registered</dt><dd><fmt:formatDate value="${student.registrationDate}" pattern="dd MMM yyyy"/></dd></div>
        </dl>
        <p class="mt-4 text-sm text-slate-500">${student.address}</p>
    </section>

    <section class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 class="text-lg font-semibold text-slate-900 mb-4">Guardian Details</h2>
        <dl class="space-y-3 text-sm text-slate-600">
            <div class="flex justify-between"><dt>Name</dt><dd>${student.guardianName}</dd></div>
            <div class="flex justify-between"><dt>Contact</dt><dd>${student.guardianContactNo}</dd></div>
            <div class="flex justify-between"><dt>Email</dt><dd>${student.guardianEmail}</dd></div>
        </dl>
    </section>
</div>

<section class="mt-8 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
    <div class="flex items-center justify-between mb-4">
        <div>
            <h2 class="text-lg font-semibold text-slate-900">Subjects & Grades</h2>
            <p class="text-sm text-slate-500">Average Grade: <span class="font-semibold">${student.averageGrade}%</span></p>
        </div>
        <form class="flex items-center gap-2" method="post" action="${pageContext.request.contextPath}/students/assign-grade">
            <input type="hidden" name="id" value="${student.id}">
            <input type="text" name="subject" placeholder="Subject" required class="rounded border border-slate-300 px-3 py-2 text-sm">
            <input type="number" step="0.1" name="grade" placeholder="Grade" required class="rounded border border-slate-300 px-3 py-2 text-sm">
            <button type="submit" class="rounded bg-sky-700 px-3 py-2 text-white text-sm">Update</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${empty student.subjectGrades}">
            <p class="text-sm text-slate-500">No grades recorded yet.</p>
        </c:when>
        <c:otherwise>
            <div class="grid gap-4 md:grid-cols-3">
                <c:forEach var="entry" items="${student.subjectGrades}">
                    <div class="rounded border border-slate-200 bg-slate-50 p-4 text-center">
                        <p class="text-sm text-slate-500 uppercase tracking-wide">${entry.key}</p>
                        <p class="text-3xl font-semibold text-slate-900">${entry.value}%</p>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<section class="mt-8 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
    <h2 class="text-lg font-semibold text-slate-900 mb-3">Notes & Remarks</h2>
    <p class="text-sm text-slate-600"><c:out value="${empty student.notes ? 'No remarks recorded yet.' : student.notes}"/></p>
</section>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

