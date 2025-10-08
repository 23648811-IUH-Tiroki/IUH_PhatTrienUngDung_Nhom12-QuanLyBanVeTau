package entity;

import java.util.Objects;

public class ChiTietLichTrinh {
	private int maChiTietLichTrinh;
	
	public ChiTietLichTrinh() {
		super();
	}

	public ChiTietLichTrinh(int maChiTietLichTrinh) {
		super();
		this.maChiTietLichTrinh = maChiTietLichTrinh;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(maChiTietLichTrinh);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietLichTrinh other = (ChiTietLichTrinh) obj;
		return maChiTietLichTrinh == other.maChiTietLichTrinh;
	}

	@Override
	public String toString() {
		return "ChiTietLichTrinh [maChiTietLichTrinh=" + maChiTietLichTrinh + "]";
	}
	
}
