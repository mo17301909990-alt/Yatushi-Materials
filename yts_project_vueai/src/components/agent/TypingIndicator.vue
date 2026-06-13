<template>
  <div class="flex items-center gap-2 px-1">
    <span class="w-1.5 h-1.5 rounded-full bg-blue-400 animate-bounce" style="animation-delay:0ms"></span>
    <span class="w-1.5 h-1.5 rounded-full bg-blue-400 animate-bounce" style="animation-delay:150ms"></span>
    <span class="w-1.5 h-1.5 rounded-full bg-blue-400 animate-bounce" style="animation-delay:300ms"></span>
  </div>
</template>

<style scoped>
@keyframes bounce {
  0%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-4px); }
}
.animate-bounce {
  animation: bounce 1s ease-in-out infinite;
}
</style>
