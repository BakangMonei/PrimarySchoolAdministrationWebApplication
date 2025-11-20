<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${schoolClass.id == null ? 'Create Class' : 'Update Class'}"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <h1 class="text-2xl font-semibold text-slate-900">
        <c:out value="${schoolClass.id == null ? 'Create Class' : 'Update Class'}"/>
    </h1>
    <a href="${pageContext.request.contextPath}/classes"
       class="text-sm text-slate-500 hover:text-slate-700">← Back</a>
</div>

<form method="post" action="${pageContext.request.contextPath}${formAction}"
      class="grid gap-6 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
    <c:if test="${schoolClass.id != null}">
        <input type="hidden" name="id" value="${schoolClass.id}"/>
    </c:if>
    <label class="text-sm font-medium text-slate-600">Class Name
        <input type="text" name="name" value="${schoolClass.name}" required
               class="mt-1 w-full rounded border border-slate-300 px-3 py-2" placeholder="Standard 5">
    </label>
    <label class="text-sm font-medium text-slate-600">Description
        <textarea name="description" rows="3"
                  class="mt-1 w-full rounded border border-slate-300 px-3 py-2">${schoolClass.description}</textarea>
    </label>
    <label class="text-sm font-medium text-slate-600">Class Teacher
        <select name="classTeacherId" class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
            <option value="">Assign later</option>
            <c:forEach var="teacher" items="${teachers}">
                <option value="${teacher.id}" <c:if test="${schoolClass.classTeacherId == teacher.id}">selected</c:if>>
                    ${teacher.name} ${teacher.surname}
                </option>
            </c:forEach>
        </select>
    </label>
    <label class="text-sm font-medium text-slate-600">Capacity
        <input type="number" name="capacity" min="10" max="45" value="${schoolClass.capacity}"
               class="mt-1 w-full rounded border border-slate-300 px-3 py-2" required>
    </label>
    <div class="flex justify-end gap-3">
        <a href="${pageContext.request.contextPath}/classes"
           class="rounded border border-slate-300 px-4 py-2 text-slate-600">Cancel</a>
        <button type="submit" class="rounded bg-sky-700 px-5 py-2 text-white">Save Class</button>
    </div>
</form>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

