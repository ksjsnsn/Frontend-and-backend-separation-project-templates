<template>

    <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">

        <div style="flex: 1;background-color: dodgerblue">
            <el-image style="width: 100%;height: 100%" fit="cover"
                      src="https://images.pexels.com/photos/5470586/pexels-photo-5470586.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2">

            </el-image>
        </div>

        <div class="welcome-title">
            <div style="font-size: 30px;font-weight: bold">欢迎来到我们的学习平台</div>
            <div style="margin-top: 10px">在这里你可以学习如何使用java，如何搭建网站，并且与java之父密切交流</div>
            <div style="margin-top: 5px">在这里你可以同性交友，因为没有学java的女孩</div>
        </div>

        <div style="width: 400px;  background-color: white;z-index: 1">
            <router-view v-slot="{ Component }">
                <transition name="el-fade-in-linear">
                    <component :is="Component"/>
                </transition>
            </router-view>
        </div>

    </div>
</template>

<script setup>

</script>

<style scoped>
.welcome-title {
    position: absolute;
    bottom: 40px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;
}
</style>