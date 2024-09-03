import { Platform } from "react-native";
import { UnavailabilityError } from "expo-modules-core";

import AlipushModule from "./AlipushModule";
import { CustomNotificationConfig } from "./AlipushModule.types";

export function init(): Promise<void> {
  if (!AlipushModule.init) {
    throw new UnavailabilityError("expo-alipush", "init");
  }
  return AlipushModule.init();
}

export function register(deviceToken?: string): Promise<void> {
  if (!AlipushModule.register) {
    throw new UnavailabilityError("expo-alipush", "register");
  }
  if (Platform.OS === "ios") {
    if (!deviceToken) {
      throw new Error(
        "[expo-alipush] deviceToken is required, the value can be get by expo-notifications"
      );
    }
    return AlipushModule.register(deviceToken);
  }
  return AlipushModule.register();
}

export function getDeviceId(): string {
  if (!AlipushModule.getDeviceId) {
    throw new UnavailabilityError("expo-alipush", "getDeviceId");
  }
  return AlipushModule.getDeviceId();
}

export function bindAccount(account: string): Promise<void> {
  if (!AlipushModule.bindAccount) {
    throw new UnavailabilityError("expo-alipush", "bindAccount");
  }
  return AlipushModule.bindAccount(account);
}

export function unbindAccount(): Promise<void> {
  if (!AlipushModule.unbindAccount) {
    throw new UnavailabilityError("expo-alipush", "unbindAccount");
  }
  return AlipushModule.unbindAccount();
}

/**
 * Android only
 *
 */
export function setCustomNotificationConfig(
  id: number,
  config: CustomNotificationConfig
): Promise<void> {
  if (!AlipushModule.setCustomNotificationConfig) {
    throw new UnavailabilityError(
      "expo-alipush",
      "setCustomNotificationConfig"
    );
  }
  return AlipushModule.setCustomNotificationConfig(id, config);
}

export function checkPushChannelStatus() {
  if (!AlipushModule.checkPushChannelStatus) {
    throw new UnavailabilityError("expo-alipush", "checkPushChannelStatus");
  }
  return AlipushModule.checkPushChannelStatus();
}

export function turnOnPushChannel() {
  if (!AlipushModule.turnOnPushChannel) {
    throw new UnavailabilityError("expo-alipush", "turnOnPushChannel");
  }
  return AlipushModule.turnOnPushChannel();
}

export function turnOffPushChannel() {
  if (!AlipushModule.turnOffPushChannel) {
    throw new UnavailabilityError("expo-alipush", "turnOffPushChannel");
  }
  return AlipushModule.turnOffPushChannel();
}
