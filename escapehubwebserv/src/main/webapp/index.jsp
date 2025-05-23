<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>EscapeHub</title>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-to-b from-gray-900 via-gray-800 to-gray-900 min-h-screen flex items-center justify-center text-white">

  <div class="border border-yellow-500 shadow-2xl rounded-2xl bg-gray-800 bg-opacity-90 p-10 max-w-md w-full text-center space-y-6">
    <h1 class="text-4xl font-extrabold text-yellow-400 tracking-wide">
      🗝️ EscapeHub
    </h1>
    <p class="text-gray-300 text-sm">
      “잠겨 있는 문, 숨겨진 단서, 그리고 제한된 시간…<br>
      당신은 탈출할 수 있을까?”
    </p>

    <div class="flex flex-col gap-4 mt-6">
      <a href="login.jsp"
         class="bg-yellow-400 hover:bg-yellow-500 text-black font-bold py-2 px-4 rounded-xl transition duration-200">
         🔐 로그인
      </a>
      <a href="register.jsp"
         class="bg-gray-200 hover:bg-gray-300 text-gray-900 font-bold py-2 px-4 rounded-xl transition duration-200">
         📝 회원가입
      </a>
    </div>

    <footer class="text-xs text-gray-500 mt-8">
      © 2025 EscapeHub. 모든 방탈출은 상상 속에서만 진행됩니다.
    </footer>
  </div>

</body>
</html>
