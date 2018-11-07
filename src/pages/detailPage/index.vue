<template>
  <div class="vist-userInfo">
    <div class="imgShow">
      <img class="productImg" :src="proInfos.filePath" alt="" mode="aspectFit">
    </div>
    <div class="productPrice">
      ¥ {{proInfos.prices}}
    </div>
    <div class="productTitle">
      {{proInfos.productName}}
    </div>
    <div class="productDetail">
      <div class="productTopNav">
        <div class="productNav" @click="detailContent('0')">
          <p :class="{ bottomLines: currentNum==0 }">概述</p>
        </div>
        <div class="productNav" @click="detailContent('1')">
          <p :class="{ bottomLines: currentNum==1}">规格</p>
        </div>
        <div class="productNav" @click="detailContent('2')">
          <p :class="{ bottomLines: currentNum==2}">视频</p>
        </div>

      </div>
      <scroll-view scroll-y :scroll-top=scrollTop :class="{detailText: currentNum==0}">
        <div>
          <p>{{proInfos.productSummary}}</p>
        </div>
      </scroll-view>
      <scroll-view scroll-y :scroll-top=scrollTop :class="{detailText: currentNum==1}">
        <div>
          <p>{{proInfos.productSpec}}</p>
        </div>
      </scroll-view>
      <scroll-view scroll-y :scroll-top=scrollTop :class="{detailText: currentNum==2}">
        <div>
            <div class="videoPlay" @click="videoPlays">
              <img src="/static/img/videoPlay.jpg" alt="">
              <p style="text-align: center;font-size: 16px;">视频上传中</p>
            </div>
        </div>
      </scroll-view>
    </div>
    <div class="customized" @click="startCustom(proInfos.productId)">
      <i class="icon iconfont icon-shangwutubiao-"></i>
      开始定制
    </div>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        productId:'',
        currentNum: 0,
        scrollTop: 0,
        proInfos:''
      }
    },

    onLoad(option) {
      var that = this;
      that.currentNum = 0;
      console.log(option)
      console.log(22222222)
      that.productId = option.productId
      that.proInfos = ''
      that.$store.state.board.productImage = ''
      wx.request({
        url: that.$store.state.board.urlHttp +'/wechatapi/product/getProductById',
        method: "POST",
        data:  {productId:option.productId,sessionID:that.$store.state.board.sessionID},
        header: {'content-type': 'application/x-www-form-urlencoded'},
        success: function (res) {
          console.log(res)
          if (res.data.success) {
            if(res.data.data){
              that.proInfos = res.data.data;
              that.proInfos.filePath = that.$store.state.board.urlHttp + that.proInfos.filePath
              that.$store.state.board.productImage = that.proInfos.filePath
              that.$store.state.board.computerInfoName  = that.proInfos.productName
            }
          }else{
            wx.showToast({
              title: '获取详细信息失败。',
              icon: 'none',
              duration: 1000
            })
          }
        }
      })
    },
    // onShareAppMessage(res) {
    //   if (res.from === 'button') {
    //     // 来自页面内转发按钮
    //     console.log(res.target)
    //   }
    //
    //   return {
    //     title: '分享活动',
    //     path: "/pages/detailPage/main?productId="+this.productId ,
    //     success: function () {
    //
    //     }
    //   }
    //
    // },
    methods: {
      detailContent(index) {
        this.currentNum = index;
        this.scrollTop = 0;
      },
      startCustom(index) {
        wx.navigateTo({
          url: '../productConfig/main?productId=' + index
        })
      },
      videoPlays(){
        wx.showToast({
          title: '无视频文件',
          icon: 'none',
          duration: 1000
        })
      }
    },
    async onPullDownRefresh() {
      // 停止下拉刷新
      wx.stopPullDownRefresh();
    },

    created() {
    },
    computed: {},
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
    //background-color: #fff;
    font-family: Arial !important;
    /*background-color: red;*/
    .imgShow {
      width: 100%;
      height: 200px;
      text-align: center;

    }
    .productImg {
      width: 160px;
      height: 160px;
      margin: 20px auto;
    }
    .productPrice {
      color: rgba(221,168,49,1);
      font-size: 16px;
      margin-left: 20px;
    }
    .productTitle {
      color: rgba(140,140,140,1);
      font-size: 20px;
      text-align: left;
      margin-left: 20px;
    }
    .productDetail {
      height: calc(100% - 305px);
      margin-top: 13px;
      .productTopNav {
        width: 100%;
        height: 40px;
        .productNav {
          float: left;
          font-size: 16px;
          width: 33.3333333%;
          height: 38px;
          line-height: 38px;
          text-align: center;
          border-bottom: 1px solid #F5F5F5;
          p{
            width: calc(100% - 40px);
            color:rgba(140,140,140,1);
            margin: 0 auto;
          }
          p.bottomLines {
            border-bottom: 2px solid #0096D6;
          }
        }

      }
      scroll-view {
        height: calc(100% - 40px);
        display: none;
        div {
          padding: 10px 10px;
          color: #575757;
          font-size: 12px;
          text-align: left;
          line-height: 17px;
          .videoPlay{
            padding: 10px;
            img{
              width: 100%;
              height: 190px;
            }
          }
        }
      }
      scroll-view.detailText {
        display: block;
      }
    }
    .customized {
      width: 100%;
      height: 50px;
      line-height: 50px;
      position: fixed;
      left: 0px;
      bottom: 0px;
      color: #fff;
      font-size: 18px;
      text-align: center;
      background-color: #0096D6;
      i {
        display: inline-block;
        font-size: 22px;
        position: relative;
        top: 2px;
      }
    }

  }
</style>
