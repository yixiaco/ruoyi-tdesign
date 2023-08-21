<template>
  <div></div>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'Redirect',
});
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const { params, query } = route;
const { path } = params;

router.replace({ path: `/${path}`, query });
</script>
