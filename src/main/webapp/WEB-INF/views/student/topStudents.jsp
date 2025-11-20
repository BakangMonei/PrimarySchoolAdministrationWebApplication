<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Top Students"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <div>
        <h1 class="text-2xl font-semibold text-slate-900">Top Performing Students</h1>
        <p class="text-sm text-slate-500">Showing top 5 learners for <strong>${selectedClass}</strong>.</p>
    </div>
    <a href="${pageContext.request.contextPath}/students"
       class="text-sm text-slate-500 hover:text-slate-700">← Back to Students</a>
</div>

<div class="grid gap-4 md:grid-cols-2">
    <c:forEach var="student" items="${topStudents}">
        <article class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <div class="flex items-center justify-between">
                <div>
                    <h2 class="text-lg font-semibold text-slate-900">
                        <c:out value="${student.name}"/> <c:out value="${student.surname}"/>
                    </h2>
                    <p class="text-sm text-slate-500">Average Grade</p>
                </div>
                <span class="text-4xl font-bold text-emerald-600">${student.averageGrade}%</span>
            </div>
            <p class="mt-3 text-sm text-slate-600">Guardian: ${student.guardianName} • ${student.guardianContactNo}</p>
            <a href="${pageContext.request.contextPath}/students/view?id=${student.id}"
               class="mt-4 inline-flex items-center text-sky-700 text-sm hover:underline">
                View Profile →
            </a>
        </article>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

