import { InCartProduct } from './inCartProduct.model';

export interface User {
  id: bigint;
  name: string;
  inCartProducts: Array<InCartProduct>;
  imageId: number;

}
