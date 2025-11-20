<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="genders" value="${fn:split('Male,Female', ',')}"/>
<c:set var="statuses" value="${fn:split('Active,Inactive,Transferred,Graduated', ',')}"/>
<c:set var="pageTitle" value="${student.id == null ? 'New Student' : 'Edit Student'}"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="flex items-center justify-between mb-6">
    <h1 class="text-2xl font-semibold text-slate-900">
        <c:out value="${student.id == null ? 'Register New Student' : 'Update Student Profile'}"/>
    </h1>
    <a href="${pageContext.request.contextPath}/students"
       class="text-sm text-slate-500 hover:text-slate-700">← Back to list</a>
</div>

<form method="post" action="${pageContext.request.contextPath}${formAction}"
      class="grid gap-6 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
    <c:if test="${student.id != null}">
        <input type="hidden" name="id" value="${student.id}"/>
    </c:if>
    <div class="grid gap-4 md:grid-cols-2">
        <label class="text-sm font-medium text-slate-600">First Name
            <input type="text" name="name" required value="${student.name}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Surname
            <input type="text" name="surname" required value="${student.surname}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Birth Certificate #
            <input type="text" name="birthCertificateNo" required value="${student.birthCertificateNo}"
                   pattern="\\d{9}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Gender
            <select name="gender" class="mt-1 w-full rounded border border-slate-300 px-3 py-2" required>
                <option value="">Select</option>
                <c:forEach var="g" items="${genders}">
                    <option value="${g}" <c:if test="${student.gender == g}">selected</c:if>>${g}</option>
                </c:forEach>
            </select>
        </label>
        <label class="text-sm font-medium text-slate-600">Date of Birth
            <input type="date" name="dateOfBirth" required value="${student.dateOfBirth}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Registration Date
            <input type="date" name="registrationDate" required value="${student.registrationDate}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Status
            <select name="status" class="mt-1 w-full rounded border border-slate-300 px-3 py-2" required>
                <c:forEach var="status" items="${statuses}">
                    <option value="${status}" <c:if test="${student.status == status}">selected</c:if>>${status}</option>
                </c:forEach>
            </select>
        </label>
        <label class="text-sm font-medium text-slate-600">Current Class
            <input type="text" name="currentClass" required value="${student.currentClass}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2" placeholder="Standard 3">
        </label>
    </div>

    <label class="text-sm font-medium text-slate-600">Residential Address
        <textarea name="address" rows="2"
                  class="mt-1 w-full rounded border border-slate-300 px-3 py-2">${student.address}</textarea>
    </label>

    <div class="grid gap-4 md:grid-cols-3">
        <label class="text-sm font-medium text-slate-600">Guardian Name
            <input type="text" name="guardianName" required value="${student.guardianName}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Guardian Contact #
            <input type="text" name="guardianContactNo" required value="${student.guardianContactNo}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
        <label class="text-sm font-medium text-slate-600">Guardian Email
            <input type="email" name="guardianEmail" value="${student.guardianEmail}"
                   class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
        </label>
    </div>

    <label class="text-sm font-medium text-slate-600">Subjects (comma separated)
        <input type="text" name="subjects" value="${student.subjectsCsv}"
               class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
    </label>

    <label class="text-sm font-medium text-slate-600">Subject Grades (e.g., Math:85, Science:78)
        <input type="text" name="subjectGrades" value="${student.subjectGradesCsv}"
               class="mt-1 w-full rounded border border-slate-300 px-3 py-2">
    </label>

    <label class="text-sm font-medium text-slate-600">Notes / Remarks
        <textarea name="notes" rows="3"
                  class="mt-1 w-full rounded border border-slate-300 px-3 py-2">${student.notes}</textarea>
    </label>

    <div class="flex items-center justify-end gap-3">
        <a href="${pageContext.request.contextPath}/students"
           class="rounded border border-slate-300 px-4 py-2 text-slate-600">Cancel</a>
        <button type="submit"
                class="rounded bg-sky-700 px-5 py-2 text-white shadow hover:bg-sky-600">
            <c:out value="${student.id == null ? 'Save Student' : 'Update Student'}"/>
        </button>
    </div>
</form>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

