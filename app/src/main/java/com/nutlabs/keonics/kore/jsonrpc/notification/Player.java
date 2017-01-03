/*
 * Copyright 2015 Synced Synapse. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nutlabs.keonics.kore.jsonrpc.notification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nutlabs.keonics.kore.jsonrpc.ApiNotification;
import com.nutlabs.keonics.kore.jsonrpc.type.GlobalType;
import com.nutlabs.keonics.kore.utils.JsonUtils;

/**
 * All Player.* notifications
 */
public class Player {

    /**
     * Player.OnPause notification
     * Playback of a media item has been paused. If there is no ID available extra information will be provided.
     */
    public static class OnPause extends ApiNotification {
        public static final String NOTIFICATION_NAME = "Player.OnPause";

        public final NotificationsData data;

        public OnPause(ObjectNode node) {
            super(node);
            data = new NotificationsData(node.get(NotificationsData.DATA_NODE));
        }

        public String getNotificationName() { return NOTIFICATION_NAME; }
    }

    /**
     * Player.OnPlay notification
     * Playback of a media item has been started or the playback speed has changed. If there is no
     * ID available extra information will be provided.
     */
    public static class OnPlay extends ApiNotification {
        public static final String NOTIFICATION_NAME = "Player.OnPlay";

        public final NotificationsData data;

        public OnPlay(ObjectNode node) {
            super(node);
            data = new NotificationsData(node.get(NotificationsData.DATA_NODE));
        }

        public String getNotificationName() { return NOTIFICATION_NAME; }
    }

    /**
     * Player.OnSeek notification
     * The playback position has been changed. If there is no ID available extra information will
     * be provided.
     */
    public static class OnSeek extends ApiNotification {
        public static final String NOTIFICATION_NAME = "Player.OnSeek";

        public final NotificationsItem item;
        public final GlobalType.Time time;
        public final GlobalType.Time seekoffset;

        public OnSeek(ObjectNode node) {
            super(node);
            ObjectNode dataNode = (ObjectNode)node.get("data");
            item = new NotificationsItem(dataNode.get(NotificationsItem.ITEM_NODE));
            ObjectNode playerNode = (ObjectNode)dataNode.get("player");
            time = new GlobalType.Time(playerNode.get("time"));
            seekoffset = new GlobalType.Time(playerNode.get("seekoffset"));
        }

        public String getNotificationName() { return NOTIFICATION_NAME; }
    }

    /**
     * Player.OnSpeedChanged notification
     * Speed of the playback of a media item has been changed. If there is no ID available extra information will be provided.
     * be provided.
     */
    public static class OnSpeedChanged extends ApiNotification {
        public static final String NOTIFICATION_NAME = "Player.OnSpeedChanged";

        public final NotificationsData data;

        public OnSpeedChanged(ObjectNode node) {
            super(node);
            data = new NotificationsData(node.get(NotificationsData.DATA_NODE));
        }

        public String getNotificationName() { return NOTIFICATION_NAME; }
    }

    /**
     * Player.OnStop notification
     * Playback of a media item has been stopped. If there is no ID available extra information will be provided.
     */
    public static class OnStop extends ApiNotification {
        public static final String NOTIFICATION_NAME = "Player.OnStop";

        public final boolean end;
        public final NotificationsItem item;

        public OnStop(ObjectNode node) {
            super(node);
            ObjectNode dataNode = (ObjectNode)node.get("data");
            end = JsonUtils.booleanFromJsonNode(dataNode, "end");
            item = new NotificationsItem(dataNode.get(NotificationsItem.ITEM_NODE));
        }

        public String getNotificationName() { return NOTIFICATION_NAME; }
    }

    /**
     * Notification data for Player
     */
    public static class NotificationsPlayer {
        public static final String PLAYER_NODE = "player";

        public final int playerId;
        public final int speed;

        public NotificationsPlayer(JsonNode node) {
            playerId = JsonUtils.intFromJsonNode(node, "playerid");
            speed = JsonUtils.intFromJsonNode(node, "speed", 0);
        }
    }

    /**
     * General notification data
     */
    public static class NotificationsItem {
        public static final String ITEM_NODE = "item";
        /**
         * The item types
         */
        public static final String TYPE_UNKNOWN = "unknown",
                TYPE_MOVIE = "movie",
                TYPE_EPISODE = "episode",
                TYPE_MUSIC_VIDEO = "musicvideo",
                TYPE_SONG = "song",
                TYPE_PICTURE = "picture",
                TYPE_CHANNEL = "channel";

        public final String type;
        public final int id;
        public final String title;
        public final int year;
        public final int episode;
        public final int season;
        public final String showtitle;
        public final String album;
        public final String artist;
        public final int track;

        public NotificationsItem(JsonNode node) {
            type = JsonUtils.stringFromJsonNode(node, "type", TYPE_UNKNOWN);
            id = JsonUtils.intFromJsonNode(node, "speed");
            title = JsonUtils.stringFromJsonNode(node, "title");
            year = JsonUtils.intFromJsonNode(node, "year", 0);
            episode = JsonUtils.intFromJsonNode(node, "episode", 0);
            season = JsonUtils.intFromJsonNode(node, "season", 0);
            showtitle = JsonUtils.stringFromJsonNode(node, "showtitle");
            album = JsonUtils.stringFromJsonNode(node, "album");
            artist = JsonUtils.stringFromJsonNode(node, "artist");
            track = JsonUtils.intFromJsonNode(node, "track", 0);
        }
    }

    public static class NotificationsData {
        public static final String DATA_NODE = "data";

        public final NotificationsPlayer player;
        public final NotificationsItem item;

        public NotificationsData(JsonNode node) {
            item = new NotificationsItem((ObjectNode)node.get(NotificationsItem.ITEM_NODE));
            player = new NotificationsPlayer((ObjectNode)node.get(NotificationsPlayer.PLAYER_NODE));
        }
    }

}