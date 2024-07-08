import {
  StyleSheet,
  Text,
  View,
  Button,
  Platform,
  TextInput,
  FlatList,
  Pressable,
  FlatListProps,
} from "react-native";
import * as AliyunPush from "expo-aliyun-push";
import React, { useEffect, useImperativeHandle, useRef, useState } from "react";
import {
  AndroidImportance,
  getPermissionsAsync,
  requestPermissionsAsync,
  setNotificationChannelAsync,
  getDevicePushTokenAsync,
  addNotificationReceivedListener,
  addNotificationResponseReceivedListener,
  addNotificationsDroppedListener,
  type NotificationResponse,
  type Notification,
  setNotificationHandler,
  registerTaskAsync,
} from "expo-notifications";
import dayjs from "dayjs";

setNotificationHandler({
  async handleNotification(notification) {
    console.debug("handle notification", notification);
    return {
      shouldShowAlert: true,
      shouldPlaySound: true,
      shouldSetBadge: true,
    };
  },
  handleError(notificationId, error) {
    console.error(`notification ${notificationId} handle error`, error);
  },
});

AliyunPush.setCustomNotificationConfig(12, {
  isBuildWhenAppInForeground: false,
});

AliyunPush.setCustomNotificationConfig(11, {
  isBuildWhenAppInForeground: true,
});

export default function App() {
  const [hasRegistered, setHasRegistered] = useState<boolean>();

  const eventsRef = useRef<{ clear(): void }>(null);

  const renderHeader = () => {
    return (
      <View style={styles.headerContainer}>
        {Platform.OS === "android" && (
          <>
            <Text style={{ userSelect: "text" }}>
              DeviceId: {AliyunPush.getDeviceId()}
            </Text>
            <NotificationChannel />
          </>
        )}
        <View style={styles.row}>
          <Text>Register status: {hasRegistered ? "Done" : "Idle"}</Text>
          <Button
            title="Register"
            onPress={() => {
              register().then(() => setHasRegistered(true), console.error);
            }}
          />
        </View>
        <BindAccount />
        <View style={styles.row}>
          <Text>Events List</Text>
          <Button
            title="Clear Events"
            onPress={() => eventsRef.current?.clear()}
          />
        </View>
      </View>
    );
  };

  return (
    <EventsList ListHeaderComponent={renderHeader()} eventsRef={eventsRef} />
  );
}

function NotificationChannel() {
  const [channelId, setChannelId] = useState("default");
  const [name, setName] = useState("default");
  return (
    <View>
      <View style={styles.row}>
        <Text>Channel Id: </Text>
        <TextInput
          value={channelId}
          onChangeText={setChannelId}
          style={styles.inputInRow}
        />
      </View>
      <Button
        title="Set Channel"
        onPress={() => {
          setNotificationChannelAsync(channelId, {
            name: channelId,
            importance: AndroidImportance.DEFAULT,
            description: channelId,
            enableLights: true,
          }).then(
            () => alert("Set notification channel success"),
            console.error
          );
        }}
      />
    </View>
  );
}

function BindAccount() {
  const [bindedAccount, setBindedAccount] = useState<string>();
  const [accoutName, setAccountName] = useState("");
  return (
    <View>
      <Text>Current bind account: {bindedAccount ?? "(none)"}</Text>
      <View>
        <TextInput
          value={accoutName}
          onChangeText={setAccountName}
          placeholder="input your account"
        />
        <Button
          title="Bind"
          onPress={() => {
            AliyunPush.bindAccount(accoutName).then(() => {
              alert("Bind success");
              setBindedAccount(accoutName);
            }, console.error);
          }}
        />
      </View>
    </View>
  );
}

type EventItem =
  | {
      type: "notification";
      date: Date;
      notification: Notification;
    }
  | {
      type: "response";
      date: Date;
      response: NotificationResponse;
    }
  | {
      type: "dropped";
      date: Date;
    };

function EventsList(
  props: Omit<FlatListProps<EventItem>, "data" | "renderItem"> & {
    eventsRef: React.Ref<{
      clear(): void;
    }>;
  }
) {
  const [data, setData] = useState<EventItem[]>([]);
  useEffect(() => {
    const subscriptions = [
      addNotificationReceivedListener((notification) => {
        setData((data) => [
          ...data,
          {
            type: "notification",
            date: new Date(),
            notification,
          },
        ]);
      }),
      addNotificationResponseReceivedListener((response) => {
        setData((data) => [
          ...data,
          {
            type: "response",
            date: new Date(),
            response,
          },
        ]);
      }),
      addNotificationsDroppedListener(() => {
        setData((data) => [
          ...data,
          {
            type: "dropped",
            date: new Date(),
          },
        ]);
      }),
    ];
    return () => subscriptions.forEach((subscription) => subscription.remove());
  }, []);

  useImperativeHandle(
    props.eventsRef,
    () => ({
      clear() {
        setData([]);
      },
    }),
    []
  );
  return (
    <FlatList
      {...props}
      data={data}
      renderItem={({ item }) => {
        if (item.type === "notification") {
          const { notification } = item;
          return (
            <Pressable
              style={styles.itemContainer}
              onPress={() => alert(JSON.stringify(notification, undefined, 2))}
            >
              <Text>
                {dayjs(notification.date).format("HH:mm:ss")}, notification{" "}
                <Text style={styles.textAccent}>received</Text>
              </Text>
              <Text>ID: {notification.request.identifier}</Text>
              <Text>Title: {notification.request.content.title}</Text>
            </Pressable>
          );
        } else if (item.type === "response") {
          const { date, response } = item;
          return (
            <Pressable
              style={styles.itemContainer}
              onPress={() => alert(JSON.stringify(response, undefined, 2))}
            >
              <Text>
                {dayjs(date).format("HH:mm:ss")}, notification{" "}
                <Text style={styles.textAccent}>response received</Text>
              </Text>
              <Text>
                Notification ID: {response.notification.request.identifier}
              </Text>
              <Text>Action ID: {response.actionIdentifier}</Text>
            </Pressable>
          );
        } else if (item.type === "dropped") {
          const { date } = item;
          return (
            <View style={styles.itemContainer}>
              <Text>
                {dayjs(date).format("HH:mm:ss")}, notification dropped
              </Text>
            </View>
          );
        }
        return null;
      }}
    />
  );
}

// utils

async function requestNotificationPermission() {
  const { status: existingStatus } = await getPermissionsAsync();
  let finalStatus = existingStatus;
  if (existingStatus !== "granted") {
    const { status } = await requestPermissionsAsync();
    finalStatus = status;
  }
  return finalStatus;
}

async function register() {
  const permissionResult = await requestNotificationPermission();
  if (permissionResult !== "granted") {
    throw new Error("Push Notification Permission Denied");
  }

  let deviceToken: string | undefined;
  if (Platform.OS === "ios") {
    await AliyunPush.init();

    deviceToken = (await getDevicePushTokenAsync()).data;
  }

  await AliyunPush.register(deviceToken);
}

const styles = StyleSheet.create({
  headerContainer: {
    backgroundColor: "#fff",
    alignItems: "stretch",
    justifyContent: "center",
    padding: 8,
    gap: 4,
  },
  row: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  inputInRow: {
    flex: 1,
  },
  itemContainer: {
    borderRadius: 6,
    borderWidth: 1,
    borderColor: "#333",
    margin: 2,
    padding: 2,
  },
  textAccent: {
    fontWeight: "bold",
  },
});
