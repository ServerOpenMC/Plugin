package fr.communaywen.core.settings;

public class PlayerSettings {
	private String uuid;
	private int mail_accept;
	private int trade_accept;
	private int tpa_accept;
	
	public PlayerSettings(String uuid, int mail_accept, int trade_accept, int tpa_accept) {
		this.uuid = uuid;
		this.mail_accept = mail_accept;
		this.trade_accept = trade_accept;
		this.tpa_accept = tpa_accept;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public int getMail_accept() {
		return mail_accept;
	}
	
	public int getTrade_accept() {
		return trade_accept;
	}
	
	public int getTpa_accept() {
		return tpa_accept;
	}
}
