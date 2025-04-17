<template>
  <div class="post-list">
    <div v-for="post in posts" :key="post.id" class="post-item">
      <h4>{{ post.title }}</h4>
      <div class="post-meta">
        <span class="post-date">{{ post.date }}</span>
      </div>
      <p class="post-excerpt">{{ post.excerpt }}</p>
      <el-button type="text" @click="$emit('view-post', post.id)">阅读全文 →</el-button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  posts: {
    type: Array,
    required: true,
    default: () => []
  }
})

defineEmits(['view-post'])
</script>

<style scoped>
.post-list {
  display: grid;
  gap: 1.5rem;
}

.post-item {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s, box-shadow 0.3s;
}

.post-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.post-item h4 {
  margin: 0 0 0.5rem;
  color: var(--primary-dark);
  font-size: 1.2rem;
}

.post-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: var(--text-color);
  opacity: 0.7;
}

.post-excerpt {
  margin: 0.5rem 0 1rem;
  color: var(--text-color);
  line-height: 1.6;
}
</style>