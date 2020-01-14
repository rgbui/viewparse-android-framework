
(function () {
    const CHANNEL_NAME = '$$android_sock_js_bridge';
    const CHANNEL_NAME_PASV = '$$android_sock_js_bridge_pasv';
    class Sock {
        constructor() {
            this.__events = [];
        }
        post(url, data, fn) {
            if (typeof data == 'function') { fn = data; data = {} };
            window.WebViewJavascriptBridge.callHandler(
                CHANNEL_NAME,
                { url, data: JSON.stringify(data) },
                function (responseData) {
                    if (typeof responseData == 'string') responseData = JSON.parse(responseData);
                    if (typeof fn == 'function') fn(responseData);
                }
            );
        }
        on(url, fn) {
            this.__events.push({ url, fn });
        }
        once(url, fn) {
            this.__events.push({ url, fn, once: true });
        }
        off(url, fn) {
            var at;
            if (typeof url == 'fn') at = this.__events.findIndex(x => x.fn == url);
            else if (typeof url == 'string' && typeof fn == 'undefined') at = this.__events.findIndex(x => x.url == url);
            else if (typeof url == 'string' && typeof fn == 'function') at = this.__events.findIndex(x => x.url == url && x.fn == fn);
            this.__events.splice(at, 1);
        }
        emit(url, ...args) {
            var ev = this.__events.find(x => x.url == url);
            if (ev) {
                var result = ev.fn.apply(this, args);
                if (ev.once == true) this.__events.splice(this.__events.findIndex(x => x == ev), 1);
                return result;
            }
        }
    }
    var sock = new Sock();
    let bridgeConnect = function (bridge) {
        //初始化
        bridge.init(function (data, responseCallback) {
            // var data = {
            //     'Javascript Responds': 'Wee!'
            // };
            // alert("begin show message");
            // alert(message);
            // responseCallback(data);
        });
        //Android调用js方法：functionInJs方法名称需要保持一致 ，并返回给Android通知
        bridge.registerHandler(CHANNEL_NAME_PASV, function (data, responseCallback) {
            var json = typeof data == 'string' ? JSON.parse(data) : data;
            var response = { success: true };
            try {
                response.result = sock.emit(json.url, json.data);
            }
            catch (e) {
                response.success = false;
                response.message = e.message;
            }
            responseCallback(JSON.stringify(response));
        });
    }
    if (window.WebViewJavascriptBridge) bridgeConnect(window.WebViewJavascriptBridge);
    else document.addEventListener('WebViewJavascriptBridgeReady', function () { bridgeConnect(window.WebViewJavascriptBridge) }, false);
    window.Sock = sock;
})();