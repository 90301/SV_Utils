package sv.distributed3;

import sv.distributed3.LiteClient.Packager;

public interface Sendable {
	public void send(LiteClient c, Packager pack);
}
