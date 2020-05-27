package com.sid.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Round {
	private String email_user;
	private LocalDateTime round_begin,round_end;

	public Round(String email_user,LocalDateTime round_begin,LocalDateTime round_end) {
		this.email_user = email_user;
		this.round_begin = round_begin;
		this.round_end = round_end;
	}
	public Round(String email_user,String round_begin,String round_end) {
		this.email_user = email_user;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(round_begin, formatter);
		this.round_begin = dateTime;
		dateTime = LocalDateTime.parse(round_end, formatter);
		this.round_end = dateTime;
	}
	
	public String getEmail_user() {
		return email_user;
	}
	public void setEmail_user(String email_user) {
		this.email_user = email_user;
	}
	public LocalDateTime getRound_begin() {
		return round_begin;
	}
	public void setRound_begin(LocalDateTime round_begin) {
		this.round_begin = round_begin;
	}
	public LocalDateTime getRound_end() {
		return round_end;
	}
	public void setRound_end(LocalDateTime round_end) {
		this.round_end = round_end;
	}
	@Override
	public String toString() {
		return "Round [email_user=" + email_user + ", round_begin=" + round_begin + ", round_end=" + round_end + "]";
	}

}
