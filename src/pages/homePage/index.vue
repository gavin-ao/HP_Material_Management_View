<template>
  <div class="vist-userInfo" v-if="authorizeFlag === true">
    <div class="sowingMap">
      <swiper indicator-dots="true" autoplay="true" interval="5000" duration="1000" circular="true">
        <swiper-item v-for="(item,index) in imgUrls" :key="index">
          <image :src="item" class="slide-image" style="width: 100%;height: 100%;"/>
        </swiper-item>
      </swiper>
    </div>
    <div class="containers">
      <div class="leftNav">
        <scroll-view scroll-y :scroll-top=scrollTop>
          <div v-for="(item,index) in dtasets" class="modeles" :key="index" :data-parentid="item.catgCode"
               :class="{ hotModel: currentNum==item.catgCode}" @click="changeNav(item)">

            <i class="icon iconfont icon-remen1" v-if="index==0"></i>
            <i class="icon iconfont icon-diannao" v-if="index==1"></i>
            <i class="icon iconfont icon-bijibendiannao" v-if="index==2"></i>
            {{item.catgName}}
            <scroll-view scroll-y :class="{ childDis: currentNum==item.catgCode}"   v-if="item.childList&&item.childList.length>0" >
                <div class="childModel"  v-for="(items,ind) in item.childList" :key="ind"
                     :data-childid="items.catgCode"
                     :class="{ childHotModel: childCurNum==items.catgCode,childDis: currentNum==item.catgCode}"
                     @click.stop="changeChildNav(items)">
                  <p>
                    {{items.catgName}}
                  </p>
                </div>
            </scroll-view>
          </div>
        </scroll-view>
      </div>
      <div class="rightCon">
        <scroll-view scroll-y :scroll-top=scrollTop>
          <div v-for="(item,index) in productItem" :key="index" class="rightModel" :data-computername="item.productName"
               @click="detailsPage(item)">
            <img class="rightImg" :src="item.filePath" alt="" mode="aspectFit">
            <p>{{item.productName}}</p>
          </div>
        </scroll-view>
      </div>
    </div>
    <div class="cliBtn" @click="contactInfo">
      <i class="icon iconfont icon-dianhua11"></i>
      联系客服
    </div>
    <div class="maskModel" :class="{maskBlock: authorLoc== true}">
      <div class="maskContainer">
        <div class="maskText">“CTO计算器”要获取你的地理位置，是否允许？</div>
        <div class="maskBtn">
          <button @click="cancle()">取消</button>
          <button open-type="openSetting" @opensetting='handlerLocation' class="confirm">确定</button>
        </div>
      </div>
    </div>
    <div class="contactModal" :class="{maskBlock: hiddenmodalput== false}">
      <div class="maskContainer">
        <div class="maskTtitle">提示</div>
        <div class="maskText">确定拨打电话：{{phoneNumber}} 吗？</div>
        <div class="maskBtn">
          <button @click="cancle">取消</button>
          <button @click='confirm' class="confirm">确定</button>
        </div>
      </div>
    </div>
  </div>
  <div class="authorizes-userInfo" v-else-if="authorizeFlag === false">
    <!--<web-view src="https://www.hejinkai.com/51talk"></web-view>-->
    <div class="authorizes">
      <div class="logo">
        <img src="/static/images/timg.jpg" alt="">
        <p style="color: #000;font-size: 18px;">惠普</p>
        <p>CTO 计算器</p>
      </div>
      <p style="border-top:1px solid #ccc;width: 100%;margin: 60px 0 0 0;"></p>
      <div class="info">
        <p>该程序将获得以下授权</p>
        <p><span class="dot">·</span><span>获得您的公开信息（头像、昵称等）</span></p>
        <p>(取消授权您可能错过很多功能哦～)</p>
      </div>
      <div class="btn">
        <div>
          <button open-type="getUserInfo" @getuserinfo="bindGetUserInfo" class="save-btn">立即授权</button>
        </div>
      </div>
    </div>

  </div>

</template>

<script>
  import utils from "../../utils/utils.js";
  import QQMapWX from '../../../static/qqmap-wx-jssdk.min'

  export default {
    data() {
      return {
        currentNum: '',
        childCurNum: '',
        scrollTop: 0,
        authorLoc: false,
        imgUrls: [],
        dtasets: [],
        productItem: [],
        hiddenmodalput: true,
        currentPage: false,
        phoneNumber: ''
      }
    },

    onLoad(option) {
      var that = this;

    },
    onShow() {
      var that = this;
      that.hiddenmodalput = true;
      that.$store.state.board.authorizeFlag = '';
      that.$store.state.board.standardPrice = 0;
      that.dtasets = [];
      that.productItem = [];
      that.imgUrls = []
      that.phoneNumber = ''
      console.log("1111: " + new Date().getTime())
      wx.getSetting({
        success: (res) => {
          console.log(res);
          console.log(res.authSetting['scope.userLocation']);

          if (res.authSetting['scope.userInfo']) {
            // 已经授权，可以直接调用 getUserInfo 获取头像昵称
            wx.showLoading({
              title: '加载中',
            })
            that.$store.state.board.authorizeFlag = true;
            utils.login(that, false, function (sessionId) {
              that.findList(sessionId);
            })
          }
          else {
            that.$store.state.board.authorizeFlag = false;
          }
          if (res.authSetting['scope.userLocation']) {
            that.location(that, QQMapWX);
          } else {
            if (res.authSetting['scope.userLocation'] != undefined && res.authSetting['scope.userLocation'] != true) {//非初始化进入该页面,且未授权
              that.authorLoc = true;
            } else if (res.authSetting['scope.userLocation'] == undefined) {//初始化进入
              that.location(that, QQMapWX);
            }
          }
        }
      })
    },
    onHide() {
      this.currentPage = false;
    },
    onUnload() {
      this.currentPage = false;
    },
    onShareAppMessage(res) {
      if (res.from === 'button') {
        // 来自页面内转发按钮
        console.log(res.target)
      }

      return {
        title: '分享活动',
        path: "/pages/homePage/main",
        success: function () {

        }
      }

    },
    methods: {
      changeNav(parData) {
        var that = this;
        that.childCurNum = 0;
        that.scrollTop = 0;
        that.currentNum = parData.catgCode;
        if (parData.childList && parData.childList.length > 0) {
          that.changeChildNav(parData.childList[0])
        } else {
          that.changeChildNav(parData)
        }
      },
      changeChildNav(chiData,e) {
        var that = this;
        that.scrollTop = 0;
        that.childCurNum = chiData.catgCode;
        console.log(chiData)

        wx.request({
          url: that.$store.state.board.urlHttp + '/wechatapi/product/findProductByCatgCode',
          method: "POST",
          data: {catgCode: chiData.catgCode, sessionID: that.$store.state.board.sessionID},
          header: {'content-type': 'application/x-www-form-urlencoded'},
          success: function (res) {
            that.productItem = []
            if (res.data.success) {
              if (res.data.data && res.data.data.length > 0) {
                that.productItem = res.data.data;
                for (var i = 0; i < that.productItem.length; i++) {
                  that.productItem[i].filePath = that.$store.state.board.urlHttp + that.productItem[i].filePath
                }
              }
              // else{
              //   wx.showToast({
              //     title: '该导航栏下无产品列表。',
              //     icon: 'none',
              //     duration: 1000
              //   })
              // }
            } else {
              wx.showToast({
                title: '获取产品列表失败。',
                icon: 'none',
                duration: 1000
              })
            }
            console.log("6666: " + new Date().getTime())
          }
        })
      },
      detailsPage(item) {
        // this.$store.state.board.computerConfigName = item.productName
        wx.navigateTo({
          url: '../detailPage/main?productId=' + item.productId
        })
      },
      cancle() {
        this.authorLoc = false;
        this.hiddenmodalput = true
      },
      handlerLocation() {
        this.authorLoc = false;
      },
      location(that, QQMapWX) {
        var qqmapsdk;
        qqmapsdk = new QQMapWX({
          key: 'OISBZ-SUKW6-LJ7SS-MXQHI-GC5FF-CQBGM'
        });
        wx.getLocation({
          type: 'gcj02',
          altitude: true,
          success: (res) => {
            qqmapsdk.reverseGeocoder({
              location: {
                latitude: res.latitude,
                longitude: res.longitude
              },
              success: (addressRes) => {
                that.authorLoc = false;
                that.$store.state.board.address = addressRes.result.address_component.province + '' + addressRes.result.address_component.district;
                that.$store.state.board.location = addressRes.result.address_component.province;
              }
            })
          }
        })
      },
      bindGetUserInfo() {
        this.login(this);
      },
      login() {
        var that = this;
        wx.showLoading({
          title: '加载中',
        })
        utils.login(that, true, function (sessionId) {
          that.findList(sessionId);
        });
      },
      contactInfo() {
        this.hiddenmodalput = false
      },
      confirm() {
        //调出手机拨号盘
        wx.makePhoneCall({
          phoneNumber: this.phoneNumber
        })
      },
      findList(sessionId) {
        var that = this;
        wx.request({
          url: that.$store.state.board.urlHttp + '/wechatapi/product/findProDuctCatgList',
          method: "POST",
          data: {sessionID: sessionId},
          header: {'content-type': 'application/x-www-form-urlencoded'},
          success: function (res) {
            console.log(res)
            wx.hideLoading()
            that.currentPage = true
            if (res.data.success) {
              console.log("5555: " + new Date().getTime())
              var filePathList = res.data.filePathList;
              that.imgUrls = [];
              for (var i = 0; i < filePathList.length; i++) {
                var urls = that.$store.state.board.urlHttp + filePathList[i];
                that.imgUrls.push(urls)
              }
              that.phoneNumber = res.data.mobilePhone;
              if (res.data.data && res.data.data.length > 0) {
                that.currentNum = res.data.data[0].catgCode;
                that.dtasets = res.data.data;
                that.changeNav(res.data.data[0])
              }
            } else {
              wx.showToast({
                title: '获取导航失败。',
                icon: 'none',
                duration: 1000
              })
            }
          }
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
      authorizeFlag() {
        return this.$store.state.board.authorizeFlag
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
    .sowingMap {
      width: 100%;
      height: 98px;
      swiper {
        width: 100%;
        height: 100%;
      }
    }
    .containers {
      width: 100%;
      height: calc(100% - 148px);

      .leftNav {
        width: 100px;
        height: 100%;
        float: left;
        background-color: rgba(35,35,35,1);
        scroll-view {
          height: 100%;
          .modeles {
            width: 100%;
            /*height: 50px;*/
            line-height: 49px;
            font-size: 14px;
            color: rgba(255,255,255,1);
            border-bottom: 1px solid #fff;
            text-align: left;
            box-sizing: border-box;
            i {
              margin-left: 10px;
              display: inline-block;
              margin-right: 2px;
              height: 30px;
              position: relative;
              top: 2px;
            }
            .icon-bijibendiannao {
              width: 17px;
              font-size: 19px;
            }

            scroll-view {
              height: 300px;
              display: none;
              .childModel {
                width: 100%;
                /*height: 50px;*/
                line-height: 49px;
                font-size: 12px;
                color: rgba(255,255,255,1);
                text-align: center;
                border-bottom: 1px solid #F5F5F5;
                box-sizing: border-box;
                display: none;
                background-color: rgba(35,35,35,1);
                p {
                  line-height: 50px;
                  word-wrap: break-word;
                  word-break: break-all;
                  white-space: nowrap;
                  overflow: hidden;
                  text-overflow: ellipsis;

                }

              }
              .childHotModel {
                color: rgba(255,255,255,1);
                background-color: rgba(0,0,0,1);
                border-left: 2px solid #0096D6;
              }
              div.childDis {
                display: block;
              }
            }
            scroll-view.childDis{
              display: block;
            }

          }
          .hotModel {
            background-color: rgba(35,35,35,1);
            color: #fff;
            border-bottom: 0px;
          }
        }

      }
      .rightCon {
        float: left;
        width: calc(100% - 100px);
        height: 100%;
        /*background-color: #fff;*/
        scroll-view {
          height: 100%;
          .rightModel {
            width: 256px;
            height: 160px;
            margin: 20px auto;
            background: -webkit-linear-gradient(left,rgba(26,26,26,1),rgba(62,62,62,1), rgba(26,26,26,1)); /* Safari 5.1 - 6.0 */
            background: -o-linear-gradient(left,rgba(26,26,26,1),rgba(62,62,62,1), rgba(26,26,26,1)); /* Opera 11.1 - 12.0 */
            background: -moz-linear-gradient(left,rgba(26,26,26,1),rgba(62,62,62,1), rgba(26,26,26,1)); /* Firefox 3.6 - 15 */
            background: linear-gradient(to right,rgba(26,26,26,1),rgba(62,62,62,1), rgba(26,26,26,1)); /* 标准的语法 */
            text-align: center;
            .rightImg {
              width: 242px;
              height: 100px;
              margin: 0 auto;
              margin-top: 20px;
            }
            p {
              height: 23px;
              margin-top: 5px;
              font-size: 10px;
              color: rgba(140,140,140,1);
              line-height: 15px;
            }
          }
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
    .maskModel {
      width: 100%;
      height: 100%;
      display: none;
      background-color: rgba(0, 0, 0, .7);
      position: fixed;
      left: 0px;
      top: 0px;
      z-index: 1000;
      .maskContainer {
        width: 80%;
        height: 110px;
        margin: 0 auto;
        margin-top: 50%;
        background-color: #fff;
        border-radius: 10px;
        .maskText {
          padding: 20px;
          padding-bottom: 10px;
          font-size: 14px;
          text-align: center;
          color: #999;
        }
        .maskBtn {
          button::after {
            border: none;
          }
          button {
            width: 50%;
            float: left;
            border-radius: 0px;
            box-sizing: border-box;
            border-top: 1px solid #ccc;
            background-color: #fff;
            border-bottom-left-radius: 10px;
          }
          .confirm {
            color: #3CC51F;
            border-left: 1px solid #ccc;
            border-bottom-left-radius: 0px;
            border-bottom-right-radius: 10px;
          }
        }
      }
    }
    .contactModal {
      width: 100%;
      height: 100%;
      display: none;
      background-color: rgba(0, 0, 0, .7);
      position: fixed;
      left: 0px;
      top: 0px;
      z-index: 1000;
      .maskContainer {
        width: 80%;
        height: 135px;
        margin: 0 auto;
        margin-top: 50%;
        background-color: #fff;
        border-radius: 10px;
        .maskTtitle {
          padding: 20px;
          padding-bottom: 10px;
          font-size: 18px;
          text-align: center;
          color: #000;
          font-weight: bold;
        }
        .maskText {
          padding-bottom: 20px;
          font-size: 14px;
          text-align: center;
          color: #999;
        }
        .maskBtn {
          button::after {
            border: none;
          }
          button {
            width: 50%;
            float: left;
            border-radius: 0px;
            box-sizing: border-box;
            border-top: 1px solid #ccc;
            background-color: #fff;
            color: #000;
            border-bottom-left-radius: 10px;
          }
          .confirm {
            color: #3CC51F;
            border-left: 1px solid #ccc;
            border-bottom-left-radius: 0px;
            border-bottom-right-radius: 10px;
          }
        }
      }
    }
    div.maskBlock {
      display: block;
    }
  }

  .authorizes-userInfo {
    padding: 40px;
    background-color: #fff;
    justify-content: initial;
    .authorizes {
      margin: 0 auto;
      font-size: 14px;
      color: #ccc;
      .logo {
        img {
          display: block;
          width: 70px;
          height: 70px;
          margin: 0 auto;
        }
        p {
          text-align: center;
          margin-top: 5px;
        }
      }
      .info {
        width: 100%;
        font-size: 12px;
        padding: 10px;
        box-sizing: border-box;
        p {
          line-height: 24px;
          span {
            display: inline-block;
            vertical-align: middle;
          }
          .dot {
            /*font-size: 12px;*/
            font-weight: bold;
            padding-right: 5px;
          }
        }
      }
      .btn {
        width: 90%;
        margin: 0 auto;
        margin-top: 60px;
        div {
          width: 100%;
          display: inline-block;
          text-align: center;
          button {
            font-size: 14px;
            font-family: "Microsoft YaHei UI Light";
            border: 0;
            margin: 0 auto;
          }
          .save-btn {
            width: 100%;
            height: 40px;
            line-height: 40px;
            text-align: center;
            margin-top: 15px;
            border-radius: 3px;
            box-sizing: border-box;
            background-color: #0096D6;
            color: #fff;
          }
          button[type="default"] {
            background-color: #f1f1f1;
            color: #000;
          }
        }
      }
    }
  }
</style>
