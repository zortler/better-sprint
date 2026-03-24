package ch.zorty.sprintFix;

public class PlayerState {

    private MovementSpeedAttribute last_mvmt_speed_packet = null;
    private byte last_sent_metadata_packet = (byte) 0x00;
    private byte last_paper_metadata_packet = (byte) 0x00;
    private boolean elytra_pose = false;


    public void set_last_mvmt_speed_packet(MovementSpeedAttribute msa) { this.last_mvmt_speed_packet = msa; }

    public MovementSpeedAttribute get_last_mvmt_speed_packet() { return this.last_mvmt_speed_packet; }

    public void set_last_metadata_packet(byte b) { this.last_sent_metadata_packet = b; }

    public byte get_last_metadata_packet() { return this.last_sent_metadata_packet; }

    public void set_last_paper_packet(byte b) { this.last_paper_metadata_packet = b; }

    public byte get_last_paper_packet() { return this.last_paper_metadata_packet; }

    public void set_elytra_pose(boolean bool) { this.elytra_pose = bool; }

    public boolean get_elytra_pose() { return this.elytra_pose; }

}
