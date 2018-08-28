function login(that, flag, func) {
  wx.login({
    success: function (res) {
      var code = res.code;
      wx.getUserInfo({
        lang: "zh_CN",
        success: function (res) {
          that.$store.state.board.nickName = res.userInfo.nickName;
          that.$store.state.board.avatarUrl = res.userInfo.avatarUrl;
          var url = that.$store.state.board.urlHttp + '/wechatapi/service/newLogin';
          console.log(new Date().getTime())
          wx.request({
            url: url,
            method: "POST",
            data: {
              appid: that.$store.state.board.appid,
              secret: that.$store.state.board.secret,
              code: code,
              encryptedData: res.encryptedData,
              iv: res.iv
            },
            header: {'content-type': 'application/x-www-form-urlencoded'},
            success: function (res) {
              console.log(new Date().getTime())
              if (res.data.success) {
                that.$store.state.board.sessionID = res.data.sessionID;
                if (flag) {
                  that.$store.state.board.authorizeFlag = true;
                }
                func(that.$store.state.board.sessionID)
              }
            }
          })
        }
      })
    }
  })
}

module.exports = {
  login: login
}
