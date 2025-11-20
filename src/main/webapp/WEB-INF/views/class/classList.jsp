<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Classes"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <div>
        <h1 class="text-2xl font-semibold text-slate-900">Classes</h1>
        <p class="text-sm text-slate-500">Allocate teachers and manage capacity across the school.</p>
    </div>
    <a href="${pageContext.request.contextPath}/classes/new"
       class="rounded-lg bg-sky-700 px-4 py-2 text-white shadow hover:bg-sky-600">Create Class</a>
</div>

<div class="grid gap-4 md:grid-cols-2">
    <c:forEach var="classObj" items="${classes}">
        <article class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <div class="flex items-center justify-between">
                <div>
                    <h2 class="text-xl font-semibold text-slate-900">${classObj.name}</h2>
                    <p class="text-sm text-slate-500">${classObj.description}</p>
                </div>
                <div class="flex gap-2">
                    <a href="${pageContext.request.contextPath}/classes/edit?id=${classObj.id}"
                       class="rounded border border-slate-300 px-3 py-1 text-sm text-slate-600">Edit</a>
                    <form method="post" action="${pageContext.request.contextPath}/classes/delete"
                          onsubmit="return confirm('Delete this class?');">
                        <input type="hidden" name="id" value="${classObj.id}">
                        <input type="hidden" name="name" value="${classObj.name}">
                        <button type="submit" class="rounded bg-rose-600 px-3 py-1 text-sm text-white">Delete</button>
                    </form>
                </div>
            </div>
            <dl class="mt-4 flex gap-6 text-sm text-slate-600">
                <div><dt class="font-semibold text-slate-500">Class Teacher</dt><dd>${classObj.classTeacherId}</dd></div>
                <div><dt class="font-semibold text-slate-500">Capacity</dt><dd>${classObj.capacity} learners</dd></div>
            </dl>
        </article>
    </c:forEach>
    <c:if test="${empty classes}">
        <p class="rounded border border-slate-200 bg-white px-4 py-6 text-center text-sm text-slate-500 col-span-2">No classes captured yet.</p>
    </c:if>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

