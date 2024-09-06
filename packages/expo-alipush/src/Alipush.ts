import { Platform } from "react-native";
import { UnavailabilityError } from "expo-modules-core";

import AlipushModule from "./AlipushModule";
import { CustomNotificationConfig } from "./AlipushModule.types";

/**
 * init alipush, should called only once in app's lifecycle
 *
 * @platform android,ios
 */
export function init(): Promise<void> {
  if (!AlipushModule.init) {
    throw new UnavailabilityError("expo-alipush", "init");
  }
  return AlipushModule.init();
}

/**
 * register deviceToken
 *
 * @param deviceToken required in ios
 * @platform ios
 */
export function register(deviceToken: string): Promise<void> {
  if (!AlipushModule.register) {
    throw new UnavailabilityError("expo-alipush", "register");
  }
  return AlipushModule.register(deviceToken);
}

/**
 * get the device id generate by alipush
 *
 * @platform android,ios
 */
export function getDeviceId(): string {
  if (!AlipushModule.getDeviceId) {
    throw new UnavailabilityError("expo-alipush", "getDeviceId");
  }
  return AlipushModule.getDeviceId();
}

/**
 * bind account to alipush
 *
 * @param account
 * @platform android, ios
 */
export function bindAccount(account: string): Promise<void> {
  if (!AlipushModule.bindAccount) {
    throw new UnavailabilityError("expo-alipush", "bindAccount");
  }
  return AlipushModule.bindAccount(account);
}

/**
 * unbind account
 *
 * @platform android, ios
 */
export function unbindAccount(): Promise<void> {
  if (!AlipushModule.unbindAccount) {
    throw new UnavailabilityError("expo-alipush", "unbindAccount");
  }
  return AlipushModule.unbindAccount();
}

/**
 * config custom notification
 * @see https://help.aliyun.com/document_detail/2834944.html
 *
 * @param id
 * @param config
 *
 * @platform android
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

/**
 * get current status of push channel
 *
 * @returns status of push channel, will be enum "on" or "off"
 *
 * @platform android
 */
export function checkPushChannelStatus() {
  if (!AlipushModule.checkPushChannelStatus) {
    throw new UnavailabilityError("expo-alipush", "checkPushChannelStatus");
  }
  return AlipushModule.checkPushChannelStatus();
}

/**
 * turn on alipush channel
 *
 * @platform android
 */
export function turnOnPushChannel() {
  if (!AlipushModule.turnOnPushChannel) {
    throw new UnavailabilityError("expo-alipush", "turnOnPushChannel");
  }
  return AlipushModule.turnOnPushChannel();
}

/**
 * turn off alipush channel
 *
 * @platform android
 */
export function turnOffPushChannel() {
  if (!AlipushModule.turnOffPushChannel) {
    throw new UnavailabilityError("expo-alipush", "turnOffPushChannel");
  }
  return AlipushModule.turnOffPushChannel();
}
