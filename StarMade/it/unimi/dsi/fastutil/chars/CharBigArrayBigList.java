/*     */ package it.unimi.dsi.fastutil.chars;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigArrays;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ public class CharBigArrayBigList extends AbstractCharBigList
/*     */   implements RandomAccess, Cloneable, Serializable
/*     */ {
/*     */   public static final long serialVersionUID = -7046029254386353130L;
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   protected static final long ONEOVERPHI = 106039L;
/*     */   protected transient char[][] a;
/*     */   protected long size;
/*     */   private static final boolean ASSERTS = false;
/*     */ 
/*     */   protected CharBigArrayBigList(char[][] a, boolean dummy)
/*     */   {
/*  89 */     this.a = a;
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList(long capacity)
/*     */   {
/*  97 */     if (capacity < 0L) throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
/*  98 */     this.a = CharBigArrays.newBigArray(capacity);
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList()
/*     */   {
/* 103 */     this(16L);
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList(CharCollection c)
/*     */   {
/* 110 */     this(c.size());
/* 111 */     for (CharIterator i = c.iterator(); i.hasNext(); add(i.nextChar()));
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList(CharBigList l)
/*     */   {
/* 118 */     this(l.size64());
/* 119 */     l.getElements(0L, this.a, 0L, this.size = l.size64());
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList(char[][] a)
/*     */   {
/* 131 */     this(a, 0L, CharBigArrays.length(a));
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList(char[][] a, long offset, long length)
/*     */   {
/* 145 */     this(length);
/* 146 */     CharBigArrays.copy(a, offset, this.a, 0L, length);
/* 147 */     this.size = length;
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList(Iterator<? extends Character> i)
/*     */   {
/* 154 */     this();
/* 155 */     while (i.hasNext()) add((Character)i.next());
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList(CharIterator i)
/*     */   {
/* 162 */     this();
/* 163 */     while (i.hasNext()) add(i.nextChar());
/*     */   }
/*     */ 
/*     */   public char[][] elements()
/*     */   {
/* 170 */     return this.a;
/*     */   }
/*     */ 
/*     */   public static CharBigArrayBigList wrap(char[][] a, long length)
/*     */   {
/* 179 */     if (length > CharBigArrays.length(a)) throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + CharBigArrays.length(a) + ")");
/* 180 */     CharBigArrayBigList l = new CharBigArrayBigList(a, false);
/* 181 */     l.size = length;
/* 182 */     return l;
/*     */   }
/*     */ 
/*     */   public static CharBigArrayBigList wrap(char[][] a)
/*     */   {
/* 190 */     return wrap(a, CharBigArrays.length(a));
/*     */   }
/*     */ 
/*     */   public void ensureCapacity(long capacity)
/*     */   {
/* 198 */     this.a = CharBigArrays.ensureCapacity(this.a, capacity, this.size);
/*     */   }
/*     */ 
/*     */   private void grow(long capacity)
/*     */   {
/* 208 */     this.a = CharBigArrays.grow(this.a, capacity, this.size);
/*     */   }
/*     */ 
/*     */   public void add(long index, char k) {
/* 212 */     ensureIndex(index);
/* 213 */     grow(this.size + 1L);
/* 214 */     if (index != this.size) CharBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
/* 215 */     CharBigArrays.set(this.a, index, k);
/* 216 */     this.size += 1L;
/*     */   }
/*     */ 
/*     */   public boolean add(char k) {
/* 220 */     grow(this.size + 1L);
/* 221 */     CharBigArrays.set(this.a, this.size++, k);
/*     */ 
/* 223 */     return true;
/*     */   }
/*     */   public char getChar(long index) {
/* 226 */     if (index >= this.size) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/* 227 */     return CharBigArrays.get(this.a, index);
/*     */   }
/*     */   public long indexOf(char k) {
/* 230 */     for (long i = 0L; i < this.size; i += 1L) if (k == CharBigArrays.get(this.a, i)) return i;
/* 231 */     return -1L;
/*     */   }
/*     */   public long lastIndexOf(char k) {
/* 234 */     for (long i = this.size; i-- != 0L; ) if (k == CharBigArrays.get(this.a, i)) return i;
/* 235 */     return -1L;
/*     */   }
/*     */   public char removeChar(long index) {
/* 238 */     if (index >= this.size) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/* 239 */     char old = CharBigArrays.get(this.a, index);
/* 240 */     this.size -= 1L;
/* 241 */     if (index != this.size) CharBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
/*     */ 
/* 243 */     return old;
/*     */   }
/*     */   public boolean rem(char k) {
/* 246 */     long index = indexOf(k);
/* 247 */     if (index == -1L) return false;
/* 248 */     removeChar(index);
/*     */ 
/* 250 */     return true;
/*     */   }
/*     */   public char set(long index, char k) {
/* 253 */     if (index >= this.size) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/* 254 */     char old = CharBigArrays.get(this.a, index);
/* 255 */     CharBigArrays.set(this.a, index, k);
/* 256 */     return old;
/*     */   }
/*     */   public void clear() {
/* 259 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */   public long size64() {
/* 263 */     return this.size;
/*     */   }
/*     */   public void size(long size) {
/* 266 */     if (size > CharBigArrays.length(this.a)) ensureCapacity(size);
/* 267 */     if (size > this.size) CharBigArrays.fill(this.a, this.size, size, '\000');
/* 268 */     this.size = size;
/*     */   }
/*     */   public boolean isEmpty() {
/* 271 */     return this.size == 0L;
/*     */   }
/*     */ 
/*     */   public void trim()
/*     */   {
/* 278 */     trim(0L);
/*     */   }
/*     */ 
/*     */   public void trim(long n)
/*     */   {
/* 296 */     long arrayLength = CharBigArrays.length(this.a);
/* 297 */     if ((n >= arrayLength) || (this.size == arrayLength)) return;
/* 298 */     this.a = CharBigArrays.trim(this.a, Math.max(n, this.size));
/*     */   }
/*     */ 
/*     */   public void getElements(int from, char[][] a, long offset, long length)
/*     */   {
/* 309 */     CharBigArrays.copy(this.a, from, a, offset, length);
/*     */   }
/*     */ 
/*     */   public void removeElements(int from, int to)
/*     */   {
/* 317 */     BigArrays.ensureFromTo(this.size, from, to);
/* 318 */     CharBigArrays.copy(this.a, to, this.a, from, this.size - to);
/* 319 */     this.size -= to - from;
/*     */   }
/*     */ 
/*     */   public void addElements(int index, char[][] a, long offset, long length)
/*     */   {
/* 329 */     ensureIndex(index);
/* 330 */     CharBigArrays.ensureOffsetLength(a, offset, length);
/* 331 */     grow(this.size + length);
/* 332 */     CharBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
/* 333 */     CharBigArrays.copy(a, offset, this.a, index, length);
/* 334 */     this.size += length;
/*     */   }
/*     */   public CharBigListIterator listIterator(final int index) {
/* 337 */     ensureIndex(index);
/* 338 */     return new AbstractCharBigListIterator() {
/* 339 */       int pos = index; int last = -1;
/*     */ 
/* 340 */       public boolean hasNext() { return this.pos < CharBigArrayBigList.this.size; } 
/* 341 */       public boolean hasPrevious() { return this.pos > 0; } 
/* 342 */       public char nextChar() { if (!hasNext()) throw new NoSuchElementException(); return CharBigArrays.get(CharBigArrayBigList.this.a, this.last = this.pos++); } 
/* 343 */       public char previousChar() { if (!hasPrevious()) throw new NoSuchElementException(); return CharBigArrays.get(CharBigArrayBigList.this.a, this.last = --this.pos); } 
/* 344 */       public long nextIndex() { return this.pos; } 
/* 345 */       public long previousIndex() { return this.pos - 1; } 
/*     */       public void add(char k) {
/* 347 */         if (this.last == -1) throw new IllegalStateException();
/* 348 */         CharBigArrayBigList.this.add(this.pos++, k);
/* 349 */         this.last = -1;
/*     */       }
/*     */       public void set(char k) {
/* 352 */         if (this.last == -1) throw new IllegalStateException();
/* 353 */         CharBigArrayBigList.this.set(this.last, k);
/*     */       }
/*     */       public void remove() {
/* 356 */         if (this.last == -1) throw new IllegalStateException();
/* 357 */         CharBigArrayBigList.this.removeChar(this.last);
/*     */ 
/* 359 */         if (this.last < this.pos) this.pos -= 1;
/* 360 */         this.last = -1;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public CharBigArrayBigList clone() {
/* 366 */     CharBigArrayBigList c = new CharBigArrayBigList(this.size);
/* 367 */     CharBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
/* 368 */     c.size = this.size;
/* 369 */     return c;
/*     */   }
/*     */ 
/*     */   public boolean equals(CharBigArrayBigList l)
/*     */   {
/* 380 */     if (l == this) return true;
/* 381 */     long s = size64();
/* 382 */     if (s != l.size64()) return false;
/* 383 */     char[][] a1 = this.a;
/* 384 */     char[][] a2 = l.a;
/* 385 */     while (s-- != 0L) if (CharBigArrays.get(a1, s) != CharBigArrays.get(a2, s)) return false;
/* 386 */     return true;
/*     */   }
/*     */ 
/*     */   public int compareTo(CharBigArrayBigList l)
/*     */   {
/* 400 */     long s1 = size64(); long s2 = l.size64();
/* 401 */     char[][] a1 = this.a; char[][] a2 = l.a;
/*     */ 
/* 404 */     for (int i = 0; (i < s1) && (i < s2); i++) {
/* 405 */       char e1 = CharBigArrays.get(a1, i);
/* 406 */       char e2 = CharBigArrays.get(a2, i);
/*     */       int r;
/* 407 */       if ((r = e1 == e2 ? 0 : e1 < e2 ? -1 : 1) != 0) return r;
/*     */     }
/* 409 */     return i < s1 ? 1 : i < s2 ? -1 : 0;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 412 */     s.defaultWriteObject();
/* 413 */     for (int i = 0; i < this.size; i++) s.writeChar(CharBigArrays.get(this.a, i)); 
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 417 */     s.defaultReadObject();
/* 418 */     this.a = CharBigArrays.newBigArray(this.size);
/* 419 */     for (int i = 0; i < this.size; i++) CharBigArrays.set(this.a, i, s.readChar());
/*     */   }
/*     */ }

/* Location:           C:\Users\Raul\Desktop\StarMade\StarMade.jar
 * Qualified Name:     it.unimi.dsi.fastutil.chars.CharBigArrayBigList
 * JD-Core Version:    0.6.2
 */