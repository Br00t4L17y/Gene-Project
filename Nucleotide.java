public enum Nucleotide {
	A(0b00),
	C(0b01),
	G(0b10),
	T(0b11);

	private String name;

	private Nucleotide(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}