<template>
  <div class="vist-userInfo">
    <div class="proDescribe">
      <div class="productImg">
        <img :src="imgPath" alt="" mode="aspectFit">
      </div>
      <div class="productInfo">
        <span> {{computerInfo}}</span>
        <p v-for="(item, index) in computerInfoData" :key="index">
          {{item}}
        </p>
      </div>
    </div>
    <hr class="lines">
    <div class="configInfo">
        <div class="infoTitle">
          <i class="icon iconfont icon-xitongpeizhi"></i>推荐配置
        </div>
      <scroll-view scroll-y>
        <div v-for="(item,index) in proConfig" :key="index" :class="{ borderLines: currentNum==item.preCtoId}" @click="configDetail(productId,item)">
         {{item.showText}}
        </div>
        <!--<div :class="{ borderLines: currentNum==0}" @click="configDetail('0')">-->
          <!--标准配置（双核i3、16G DDR4、1T、集显）-->
        <!--</div>-->
        <!--<div :class="{ borderLines: currentNum==1}"  @click="configDetail('1')">-->
          <!--最受欢迎（四核i5、32G DDR4、500G、集显）-->
        <!--</div>-->
        <!--<div :class="{ borderLines: currentNum==2}"  @click="configDetail('2')">-->
          <!--日常办公（双核 i5、16G DDR4、500G、集显）-->
        <!--</div>-->
        <!--<div :class="{ borderLines: currentNum==3}"  @click="configDetail('3')">-->
          <!--编程首选（四核 i7、32G DDR4、1T、集显）-->
        <!--</div>-->
        <!--<div :class="{ borderLines: currentNum==4}"  @click="configDetail('4')">-->
          <!--绘图必备（四核 i7、32G DDR4、2T、独显）-->
        <!--</div>-->
        <!--<div :class="{ borderLines: currentNum==5}"  @click="configDetail('5')">-->
          <!--最受欢迎（四核i5、32G DDR4、500G、集显）-->
        <!--</div>-->
        <!--<div :class="{ borderLines: currentNum==6}"  @click="configDetail('6')">-->
          <!--最受欢迎（四核i5、32G DDR4、500G、集显）-->
        <!--</div>-->
        <!--<div :class="{ borderLines: currentNum==7}"  @click="configDetail('7')">-->
          <!--最受欢迎（四核i5、32G DDR4、500G、集显）-->
        <!--</div>-->
      </scroll-view>
    </div>
    <div class="cliBtn" @click="contactConfigInfo">
      <i class="icon iconfont icon-xitongpeizhi"></i>
      个性化定制
    </div>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        productId:'',
        currentNum:'',
        imgPath:'',
        proConfig:[],
        preCtoId:''
      }
    },
    onLoad(option) {
      var that = this;
      that.currentNum = '';
      that.productId = option.productId
      console.log(option.productId)
      wx.request({
        url: that.$store.state.board.urlHttp +'/wechatapi/product/getCtoProductAndPre',
        method: "POST",
        data:  {productId:option.productId,sessionID:that.$store.state.board.sessionID},
        header: {'content-type': 'application/x-www-form-urlencoded'},
        dataType:'json',
        success: function (res) {
          console.log(res)
          that.proConfig = []
          that.$store.state.board.computerInfoData=[]
          if (res.data.success) {
            console.log(res.data.details)
            that.imgPath =  that.$store.state.board.urlHttp + res.data.product.filePath;
            that.$store.state.board.computerInfoData =  res.data.details.split("\n");
            that.$store.state.board.standardPrice  = res.data.prices
            if(res.data.preCtoList&&res.data.preCtoList.length>0){
              that.currentNum = res.data.preCtoList[0].preCtoId;
              that.$store.state.board.computerConfigName = res.data.preCtoList[0].showText;
              that.proConfig = res.data.preCtoList;
            }else{
              wx.showToast({
                title: '无推荐配置',
                icon: 'none',
                duration: 1000
              })
            }
          }else{
            wx.showToast({
              title: '获取推荐配置失败，请重新获取！',
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
    //     path: "/pages/productConfig/main?productId="+this.productId,
    //     success: function () {
    //
    //     }
    //   }
    //
    // },
    methods: {
      configDetail(productId,item){
        var that= this;
        this.currentNum = item.preCtoId;
        this.$store.state.board.computerConfigName = item.showText
        wx.request({
          url: that.$store.state.board.urlHttp + '/wechatapi/product/getDetailsByPreCtoId',
          method: "POST",
          data:{productId:productId,preCtoId: item.preCtoId,sessionID:that.$store.state.board.sessionID},
          header: {'content-type': 'application/x-www-form-urlencoded'},
          success: function (res) {
            console.log(res)
            if (res.data.success) {
              that.$store.state.board.computerInfoData =  res.data.details.split("\n");
              that.$store.state.board.standardPrice  = res.data.prices
            } else {
              wx.showToast({
                title: '获取配置信息失败。',
                icon: 'none',
                duration: 1000
              })
            }
          }
        })
      },
      contactConfigInfo(){
        if(this.currentNum!==''){
          wx.navigateTo({
            url: '../configDetail/main?productId='+this.productId  +"&preCtoId="+this.currentNum
          })
        }else{
          wx.showToast({
            title: '请选择推荐配置。',
            icon: 'none',
            duration: 1000
          })
        }
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
    box-sizing:border-box;
    padding: 10px;
    .proDescribe {
      width: 100%;
      height: 160px;
      padding-bottom: 10px;
      font-family: Arial;
      div {
        float: left;
      }
      .productImg {
        width: 140px;
        height: 160px;
        img {
          display: block;
          width: 100%;
          height: 100%;
          margin: 0 auto;
        }
      }
      .productInfo {
        width: calc(100% - 165px);
        padding-left: 25px;
        span {
          display: block;
          font-size: 16px;
          color: #4A4A4A;
          line-height: 30px;
        }
        p {
          font-size: 11px;
          color: rgba(87, 87, 87, 0.9);
          line-height: 18px;
        }

      }
    }
    .lines{
      width: 100%;
      height: 1px;
      background-color: #ccc;
      margin: 10px 0px;
    }
    .configInfo{
      width: 100%;
      height: calc(100% - 243px);
      background-color: #F8F8F8;
      line-height: 20px;
      border-radius: 3px;
      text-align: center;
      border: 1px solid rgba(204, 204, 204, 1);
      box-sizing:border-box;
      .infoTitle{
        line-height: 17px;
        color: rgba(45, 45, 45, 1);
        font-size: 12px;
        text-align: left;
        font-family: Arial;
        margin: 10px;
        i{
          display: inline-block;
          position: relative;
          color: #666;
          top: 3px;
          margin-right: 3px;
        }
      }
      scroll-view{
        width: 100%;
        height: calc(100% - 37px);
        box-sizing:border-box;
        padding: 0px 10px;
        div{
          width: 100%;
          height: 50px;
          line-height: 50px;
          border-radius: 3px;
          box-sizing:border-box;
          border: 1px solid rgba(204, 204, 204, 1);
          color: rgba(16, 16, 16, 1);
          font-size: 14px;
          text-align: left;
          padding-left: 15px;
          font-family: Arial;
          background-color: #fff;
          margin-bottom: 10px;
        }
        .borderLines{
          border: 1px solid #0096D6;
        }
      }
    }
    .cliBtn {
      width: 100%;
      height: 50px;
      position: fixed;
      left: 0px;
      bottom: 0px;
      box-sizing: border-box;
      line-height: 50px;
      background-color: rgba(0, 150, 217, 1);
      color: rgba(255, 255, 255, 1);
      font-size: 18px;
      text-align: center;
      font-family: Arial;
      i {
        display: inline-block;
        font-size: 22px;
        position: relative;
        top: 3px;
      }

    }
  }
</style>
