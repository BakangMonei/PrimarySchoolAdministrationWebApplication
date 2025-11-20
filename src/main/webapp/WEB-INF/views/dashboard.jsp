<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Dashboard"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<section class="grid gap-6 md:grid-cols-3">
    <c:forEach var="stat" items="${stats}">
        <div class="rounded-xl bg-white p-6 shadow-sm border border-slate-200">
            <h3 class="text-sm uppercase tracking-wide text-slate-500">
                <c:out value="${stat.key}"/>
            </h3>
            <p class="text-4xl font-semibold text-sky-900 mt-2">
                <c:out value="${stat.value}"/>
            </p>
        </div>
    </c:forEach>
</section>
<section class="mt-10">
    <div class="rounded-xl border border-emerald-200 bg-emerald-50 p-6">
        <h2 class="text-xl font-semibold text-emerald-900 mb-2">System Highlights</h2>
        <p class="text-emerald-800">
            Use the navigation menu to manage students, teachers, and classes. Remember to keep guardians informed
            via the Parent Portal after updating learner progress.
        </p>
    </div>
</section>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

