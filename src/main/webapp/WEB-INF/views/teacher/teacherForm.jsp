<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="genders" value="${fn:split('Male,Female', ',')}"/>
<c:set var="pageTitle" value="${teacher.id == null ? 'Add Teacher' : 'Edit Teacher'}"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <h1 class="text-2xl font-semibold text-slate-900">
        <c:out value="${teacher.id == null ? 'Capture Teacher Record' : 'Update Teacher Record'}"/>
    </h1>
    <a href="${pageContext.request.contextPath}/teachers"
       class="text-sm text-slate-500 hover:text-slate-700">← Back</a>
</div>

<form method="post" action="${pageContext.request.contextPath}${formAction}"
      class="grid gap-6 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
    <c:if test="${teacher.id != null}">
        <input type="hidden" name="id" value="${teacher.id}"/>
    </c:if>
    <div class="grid gap-4 md:grid-cols-2">
        <label class="text-sm font-medium text-slate-600">First Name
            <input type="text" name="name" value="${teacher.name}" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Surname
            <input type="text" name="surname" value="${teacher.surname}" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Omang/Passport
            <input type="text" name="omang" value="${teacher.omangOrPassportNo}" required class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Gender
            <select name="gender" class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
                <option value="">Select</option>
                <c:forEach var="g" items="${genders}">
                    <option value="${g}" <c:if test="${teacher.gender == g}">selected</c:if>>${g}</option>
                </c:forEach>
            </select>
        </label>
    </div>
    <div class="grid gap-4 md:grid-cols-3">
        <label class="text-sm font-medium text-slate-600">Email
            <input type="email" name="email" value="${teacher.email}" class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Contact Number
            <input type="text" name="contactNo" value="${teacher.contactNo}" class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Date Joined
            <input type="date" name="dateJoined" value="${teacher.dateJoined}" class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
    </div>
    <label class="text-sm font-medium text-slate-600">Qualifications
        <textarea name="qualifications" rows="2" class="mt-1 w-full rounded border border-slate-300 px-3 py-2">${teacher.qualifications}</textarea>
    </label>
    <label class="text-sm font-medium text-slate-600">Subjects Qualified (comma separated)
        <input type="text" name="subjectsQualified" value="${teacher.subjectsQualifiedCsv}"
               class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
    </label>
    <label class="text-sm font-medium text-slate-600">Class → Subject Map (e.g., Standard 1:Math, Standard 2:Science)
        <input type="text" name="classSubjectMap" value="${teacher.classSubjectMapCsv}"
               class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
    </label>
    <label class="text-sm font-medium text-slate-600">Address
        <textarea name="address" rows="3" class="mt-1 w-full rounded border border-slate-300 px-3 py-2">${teacher.address}</textarea>
    </label>
    <div class="flex justify-end gap-3">
        <a href="${pageContext.request.contextPath}/teachers"
           class="rounded border border-slate-300 px-4 py-2 text-slate-600">Cancel</a>
        <button type="submit" class="rounded bg-sky-700 px-5 py-2 text-white">
            <c:out value="${teacher.id == null ? 'Save Teacher' : 'Update Teacher'}"/>
        </button>
    </div>
</form>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

