<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Primary School Portal"/>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<section class="grid gap-10 lg:grid-cols-2 items-center">
    <div class="space-y-6">
        <p class="text-sm uppercase tracking-[0.3em] text-sky-700">Welcome</p>
        <h1 class="text-4xl font-bold text-slate-900 leading-tight">
            Digitize the entire primary school administration in one secure portal.
        </h1>
        <p class="text-lg text-slate-600">
            Capture students, manage teachers, assign classes, and keep parents in the loop with live progress
            updates powered by Servlets, JSP, and JDBC.
        </p>
        <div class="flex flex-wrap gap-4">
            <a href="${pageContext.request.contextPath}/dashboard"
               class="inline-flex items-center rounded-lg bg-sky-700 px-6 py-3 text-white font-semibold shadow hover:bg-sky-600">
                Go to Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/auth/login"
               class="inline-flex items-center rounded-lg border border-slate-300 px-6 py-3 text-slate-700 font-semibold hover:border-slate-400">
                Sign In
            </a>
        </div>
    </div>
    <div class="rounded-3xl border border-slate-200 bg-white p-6 shadow-lg">
        <div class="grid gap-5">
            <article class="rounded-2xl bg-sky-50 p-5 border border-sky-100">
                <h2 class="text-xl font-semibold text-sky-900">Student Management</h2>
                <p class="text-slate-600 text-sm mt-2">Register learners, assign classes, record grades, and monitor performance trends.</p>
            </article>
            <article class="rounded-2xl bg-emerald-50 p-5 border border-emerald-100">
                <h2 class="text-xl font-semibold text-emerald-900">Teachers & Classes</h2>
                <p class="text-slate-600 text-sm mt-2">Track qualifications, class allocations, and teaching schedules with ease.</p>
            </article>
            <article class="rounded-2xl bg-amber-50 p-5 border border-amber-100">
                <h2 class="text-xl font-semibold text-amber-900">Parent Portal</h2>
                <p class="text-slate-600 text-sm mt-2">Provide secure access for guardians to follow their children’s academic journey.</p>
            </article>
        </div>
    </div>
</section>

<section class="mt-16 grid gap-6 md:grid-cols-3">
    <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <p class="text-sm text-slate-500 uppercase">Technology Stack</p>
        <h3 class="text-lg font-semibold text-slate-900 mt-2">Servlets & JSP</h3>
        <p class="text-sm text-slate-600 mt-2">Pure Jakarta EE stack with JSTL, JavaBeans, and JDBC against MySQL.</p>
    </div>
    <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <p class="text-sm text-slate-500 uppercase">Security</p>
        <h3 class="text-lg font-semibold text-slate-900 mt-2">Sessions & Cookies</h3>
        <p class="text-sm text-slate-600 mt-2">Role-based access control, Remember-Me cookies, and password hashing.</p>
    </div>
    <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <p class="text-sm text-slate-500 uppercase">User Roles</p>
        <h3 class="text-lg font-semibold text-slate-900 mt-2">Admin · Teacher · Parent</h3>
        <p class="text-sm text-slate-600 mt-2">Tailored dashboards for each persona keep everyone informed.</p>
    </div>
</section>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>