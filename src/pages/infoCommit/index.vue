<template>
  <div class="vist-userInfo">
    <div class="commitSuc">
      <p><i class="icon iconfont icon-chenggong"></i>提交成功！</p>
      <!--<p> <icon type="success" size="40"/>提交成功！</p>-->
    </div>
    <div class="commitInfo">
      <div class="commitTitle">
        <p> 您定制的产品型号是 {{computerInfo}}</p>
        <p> 具体参数如下：</p>
      </div>
      <div class="commitImg">
        <div class="img">
          <img :src="productImage" alt="" mode="aspectFit">
        </div>
        <div class="infos">
          <scroll-view scroll-y>
            <p v-for="(item, index) in computerInfoData" :key="index">
              {{item}}
            </p>
          </scroll-view>

        </div>
      </div>
      <div class="configPrice">
        <p>
          总价<span>RMB {{totalPrice}}</span>
        </p>
      </div>
    </div>
    <div class="homePage" @click="goHomePage()">
      回首页
    </div>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        totalPrice:0
      }
    },

    onLoad(option) {
      console.log(option)
      if(option.totalPrice){
        this.totalPrice = option.totalPrice
      }else{
        this.totalPrice = 0
      }

    },
    // onShareAppMessage(res) {
    //   if (res.from === 'button') {
    //     // 来自页面内转发按钮
    //     console.log(res.target)
    //   }
    //
    //   return {
    //     title: '分享活动',
    //     path: "/pages/infoCommit/main",
    //     success: function () {
    //
    //     }
    //   }
    //
    // },
    methods: {
      goHomePage() {
        // wx.navigateBack({
        //   delta: 4
        // })
        // wx.navigateTo({
        //   url: '../homePage/main'
        // })
        wx.reLaunch({
          url: '../homePage/main'
        })
      }
    },
    async onPullDownRefresh() {
      // 停止下拉刷新
      wx.stopPullDownRefresh();
    },

    created() {
    },
    computed: {
      computerInfo() {
        return this.$store.state.board.computerInfoName
      },
      computerInfoData() {
        return this.$store.state.board.computerInfoData
      },
      productImage() {
        return this.$store.state.board.productImage
      }
    },
    mounted() {
      var that = this;

    }
  }
</script>

<style lang="scss" scoped>
  .vist-userInfo {
    font-size: 22px;
    width: 100%;
    height: 100%;
    padding: 10px;
    box-sizing: border-box;
    .commitSuc {
      margin-top: 26px;
      margin-bottom: 25px;
      p {
        line-height: 29px;
        color: rgba(0, 150, 214, 1);
        font-size: 20px;
        text-align: center;
        font-family: Arial;
        i {
          display: inline-block;
          font-size: 24px;
          /*position: relative;*/
          /*top: 2px;*/
          margin-right: 10px;
        }
      }
    }
    .commitInfo {
      .commitTitle {
        margin-bottom: 30px;
        p {
          line-height: 20px;
          color: rgba(16, 16, 16, 1);
          font-size: 14px;
          text-align: left;
          font-family: Arial;
        }
      }
      .commitImg {
        width: 100%;
        height: 160px;
        margin-bottom: 43px;
        div {
          float: left;
        }
        .img {
          width: 140px;
          height: 160px;
          img {
            display: block;
            width: 100%;
            height: 100%;
            margin: 0 auto;
          }
        }
        .infos {
          width: calc(100% - 165px);
          height: 100%;
          padding-left: 25px;
          scroll-view{
            height: 100%;
            p {
              line-height: 20px;
              color: rgba(87, 87, 87, 0.9);
              font-size: 11px;
              text-align: left;
              font-family: Arial;
            }
          }

        }
      }
      .configPrice {
        width: 100%;
        p {
          height: 23px;
          line-height: 20px;
          color: rgba(16, 16, 16, 1);
          font-size: 14px;
          font-family: Arial;
          margin-left: 80px;
          span {
            display: inline-block;
            margin-left: 5px;
            color: rgba(0, 150, 214, 1);
          }

        }
      }
    }
    .homePage {
      width: 100%;
      height: 50px;
      line-height: 50px;
      position: fixed;
      left: 0px;
      bottom: 0px;
      background-color: rgba(0, 150, 214, 1);
      color: #fff;
      font-size: 16px;
      text-align: center;
      font-family: Arial;
      border: 1px solid rgba(0, 150, 214, 1);
      box-sizing: border-box;
    }
  }
</style>
