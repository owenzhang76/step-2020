package com.google.sps.data;

public final class Message {

  private final long id;
  private final String body;
  private final long timestamp;
  private final String senderName;
  private final String imageUrl;

  public Message(long id, String body, long timestamp, String senderName, String imageUrl) {
    this.id = id;
    this.body = body;
    this.timestamp = timestamp;
    this.senderName = senderName;
    this.imageUrl = imageUrl;
  }
}