package org.ps5jb.sdk.include.sys;

import org.ps5jb.sdk.core.Pointer;
import org.ps5jb.sdk.core.SdkException;
import org.ps5jb.sdk.include.sys.rtprio.RtPrioType;
import org.ps5jb.sdk.lib.LibKernel;

/**
 * This class represents <code>include/sys/rtprio.h</code> from FreeBSD source.
 */
public class RtPrio {
    private final LibKernel libKernel;
    private final ErrNo errNo;

    /**
     * Constructor.
     *
     * @param libKernel Instance of the 'libkernel' native library wrapper.
     */
    public RtPrio(LibKernel libKernel) {
        this.libKernel = libKernel;
        this.errNo = new ErrNo(this.libKernel);
    }

    public org.ps5jb.sdk.include.sys.rtprio.RtPrio lookupRtPrio(int lwpid) throws SdkException {
        Pointer buf = Pointer.calloc(4);
        try {
            int ret = libKernel.rtprio_thread(org.ps5jb.sdk.include.sys.rtprio.RtPrio.RTP_LOOKUP, lwpid, buf);
            if (ret == -1) {
                throw errNo.getLastException(getClass(), "lookupRtPrio");
            }

            return new org.ps5jb.sdk.include.sys.rtprio.RtPrio(RtPrioType.valueOf(buf.read2()), buf.read2(2));
        } finally {
            buf.free();
        }
    }

    public void setRtPrio(int lwpid, org.ps5jb.sdk.include.sys.rtprio.RtPrio rtp) throws SdkException {
        Pointer buf = Pointer.calloc(4);
        try {
            buf.write2(rtp.getType().value());
            buf.write2(2, rtp.getPriority());

            int ret = libKernel.rtprio_thread(org.ps5jb.sdk.include.sys.rtprio.RtPrio.RTP_SET, lwpid, buf);
            if (ret == -1) {
                throw errNo.getLastException(getClass(), "setRtPrio");
            }
        } finally {
            buf.free();
        }
    }
}