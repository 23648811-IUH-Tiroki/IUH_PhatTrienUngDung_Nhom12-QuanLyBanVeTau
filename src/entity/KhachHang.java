package entity;

import java.util.Objects;

public class KhachHang {
	private String maKH;
	private String tenKhachHang;
	private String soCCCD;
	private String hoChieu;
	private String soDienThoai;
	private boolean laNguoiMuaVe = true;
	private String email;
	private String diaChi;
	
	
	public KhachHang(String soCCCD_HoChieu, String tenKhachHang, boolean laNguoiNuocNgoai, String soDienThoai, String email, String diaChi) {
		this.tenKhachHang = tenKhachHang;
		this.soDienThoai = soDienThoai;
		if (laNguoiNuocNgoai == true) this.hoChieu = soCCCD_HoChieu;
		else this.soCCCD = soCCCD_HoChieu;
		this.email = email;
		this.diaChi = diaChi;
	}

	
	public KhachHang(String soCCCD_HoChieu, String tenKhachHang, boolean laNguoiNuocNgoai, String soDienThoai, boolean laNguoiMuaVe) {
		this.tenKhachHang = tenKhachHang;
		this.soDienThoai = soDienThoai;
		if (laNguoiNuocNgoai == true) this.hoChieu = soCCCD_HoChieu;
		else this.soCCCD = soCCCD_HoChieu;
		this.laNguoiMuaVe = laNguoiMuaVe;
	}

	public String getMaKH() {
		return maKH;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public String getSoCCCD() {
		return soCCCD;
	}

	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
	}

	public String getHoChieu() {
		return hoChieu;
	}

	public void setHoChieu(String hoChieu) {
		this.hoChieu = hoChieu;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKH);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKH, other.maKH);
	}

	@Override
	public String toString() {
		return "KhachHang [maKH=" + maKH + ", tenKhachHang=" + tenKhachHang + ", soCCCD=" + soCCCD + ", hoChieu=" + hoChieu + "]";
	}
	
}
