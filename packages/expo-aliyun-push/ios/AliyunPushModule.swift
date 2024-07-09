import ExpoModulesCore
import CloudPushSDK

public class AliyunPushModule: Module {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  public func definition() -> ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('AliyunPush')` in JavaScript.
    Name("AliyunPush")
    
    AsyncFunction("init") { (promise: Promise) in
      if let appKey = Bundle.main.object(forInfoDictionaryKey: "ALIYUN_EMAS_APP_KEY") as? String,
         let appSecret = Bundle.main.object(forInfoDictionaryKey: "ALIYUN_EMAS_APP_SECRET") as? String {
        
        CloudPushSDK.asyncInit(appKey, appSecret: appSecret) { result in
          if (result?.success == true) {
            promise.resolve()
          } else if let error = result?.error {
            promise.reject(error)
          } else {
            promise.reject("E_INIT", "aliyun push failed to init")
          }
        }
      } else {
        promise.reject("E_INIT_NO_KEY", "aliyun push appKey or appSecret not defined")
      }
    }
    
    AsyncFunction("register") { (deviceToken: String, promise: Promise) in
      let token = convertToData(fromHex: deviceToken)
      CloudPushSDK.registerDevice(token) { (result) in
        if (result?.success == true) {
          promise.resolve()
        } else if let error = result?.error {
          promise.reject(error)
        } else {
          promise.reject("E_REGISTER", "aliyun push failed to register")
        }
      }
    }
    
    AsyncFunction("bindAccount") { (account: String, promise: Promise) in
      CloudPushSDK.bindAccount(account) { result in
        if (result?.success == true) {
          promise.resolve()
        } else if let error = result?.error {
          promise.reject(error)
        } else {
          promise.reject("E_BIND_ACCOUNT", "aliyun push failed to bind account \(account)")
        }
      }
    }
    
    AsyncFunction("unbindAccount") { (promise: Promise) in
      CloudPushSDK.unbindAccount { result in
        if (result?.success == true) {
          promise.resolve()
        } else if let error = result?.error {
          promise.reject(error)
        } else {
          promise.reject("E_UNBIND_ACCOUNT", "aliyun push failed to unbind account")
        }
      }
    }
  }
  
  func convertToData(fromHex hexString: String) -> Data {
    let byteCharacters = Array(hexString.lowercased())
    let bytes = NSMutableData()
    
    for index in stride(from: 0, to: byteCharacters.count, by: 2) {
      let firstByte = UInt8(String(byteCharacters[index]), radix: 16)!
      let secondByte = UInt8(String(byteCharacters[index + 1]), radix: 16)!
      var combinedByte = (firstByte << 4) | secondByte
      bytes.append(&combinedByte, length: 1)
    }
    
    return bytes as Data
  }
}
