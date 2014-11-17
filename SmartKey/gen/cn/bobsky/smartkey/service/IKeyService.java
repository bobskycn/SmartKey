/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\AndroidGIT\\localGit\\SmartKey\\SmartKey\\src\\cn\\bobsky\\smartkey\\service\\IKeyService.aidl
 */
package cn.bobsky.smartkey.service;
public interface IKeyService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cn.bobsky.smartkey.service.IKeyService
{
private static final java.lang.String DESCRIPTOR = "cn.bobsky.smartkey.service.IKeyService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.bobsky.smartkey.service.IKeyService interface,
 * generating a proxy if needed.
 */
public static cn.bobsky.smartkey.service.IKeyService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cn.bobsky.smartkey.service.IKeyService))) {
return ((cn.bobsky.smartkey.service.IKeyService)iin);
}
return new cn.bobsky.smartkey.service.IKeyService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_performGlobalAction:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.performGlobalAction(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cn.bobsky.smartkey.service.IKeyService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void performGlobalAction(int action) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(action);
mRemote.transact(Stub.TRANSACTION_performGlobalAction, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_performGlobalAction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void performGlobalAction(int action) throws android.os.RemoteException;
}
