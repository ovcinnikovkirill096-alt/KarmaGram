package com.android.billingclient.api;

public class QueryProductDetailsParams$Product {
    private final String zza;
    private final String zzb;

    public static class Builder {
        private String zza;
        private String zzb;

        /* synthetic */ Builder(zzcz zzczVar) {
        }

        public QueryProductDetailsParams$Product build() {
            if ("first_party".equals(this.zzb)) {
                throw new IllegalArgumentException("Serialized doc id must be provided for first party products.");
            }
            if (this.zza == null) {
                throw new IllegalArgumentException("Product id must be provided.");
            }
            if (this.zzb != null) {
                return new QueryProductDetailsParams$Product(this, null);
            }
            throw new IllegalArgumentException("Product type must be provided.");
        }

        public Builder setProductId(String str) {
            this.zza = str;
            return this;
        }

        public Builder setProductType(String str) {
            this.zzb = str;
            return this;
        }
    }

    /* synthetic */ QueryProductDetailsParams$Product(Builder builder, zzcz zzczVar) {
        this.zza = builder.zza;
        this.zzb = builder.zzb;
    }

    public static Builder newBuilder() {
        return new Builder(null);
    }
}
