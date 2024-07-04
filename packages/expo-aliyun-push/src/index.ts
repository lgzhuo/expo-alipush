import { Platform } from "react-native";
import { UnavailabilityError } from "expo-modules-core";

import AliyunPushModule from "./AliyunPushModule";

export function init(): Promise<void> {
  if (Platform.OS !== "ios") {
    console.warn("[expo-aliyun-push] init is not support in this platform");
    return Promise.resolve();
  }
  if (!AliyunPushModule.init) {
    throw new UnavailabilityError("expo-aliyun-push", "init");
  }
  return AliyunPushModule.init();
}

export function register(deviceToken?: string): Promise<void> {
  if (!AliyunPushModule.register) {
    throw new UnavailabilityError("expo-aliyun-push", "register");
  }
  if (Platform.OS === "ios" && !deviceToken) {
    if (!deviceToken) {
      throw new Error(
        "[expo-aliyun-push] deviceToken is required, the value can be get by expo-notifications"
      );
    }
    AliyunPushModule.register(deviceToken);
  }
  return AliyunPushModule.register();
}

export function getDeviceId(): string {
  if (!AliyunPushModule.getDeviceId) {
    throw new UnavailabilityError("expo-aliyun-push", "getDeviceId");
  }
  return AliyunPushModule.getDeviceId();
}

export function bindAccount(account: string): Promise<void> {
  if (!AliyunPushModule.bindAccount) {
    throw new UnavailabilityError("expo-aliyun-push", "bindAccount");
  }
  return AliyunPushModule.bindAccount(account);
}

export function unbindAccount(): Promise<void> {
  if (!AliyunPushModule.bindAccount) {
    throw new UnavailabilityError("expo-aliyun-push", "unbindAccount");
  }
  return AliyunPushModule.unbindAccount();
}
